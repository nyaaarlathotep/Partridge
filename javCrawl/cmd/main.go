package main

import (
	"fmt"
	"github.com/antchfx/htmlquery"
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"javCrawl/internal/dal/dao"
	"javCrawl/internal/dal/query"
	"javCrawl/internal/scan"
	"log"
	"os"
	"regexp"
	"strings"
	"time"
)

// TODO transactional support
// TODO or maybe multi thread
var queries *query.Query

const urlPrefix = "https://www.javbus.com/"
const YYYYMMDD = "2006-01-02"
const javDir = "/media/disk1/Linux/test"
const (
	Jav     string = "1"
	eHentai string = "2"
)

var spaceReg = regexp.MustCompile("\\s+")

func main() {
	bT := time.Now()

	//elementQ := queries.Element
	//newElement := &dao.Element{
	//	TYPE:       Jav,
	//	SHAREDFLAG: 1,
	//	UPLOADER:   "",
	//}
	//err := elementQ.Create(newElement)
	//if err != nil {
	//	return
	//}
	//log.Printf("%+v", newElement)

	count := scanJavDir(javDir)
	log.Printf("update or insert jav num: %v", count)
	eT := time.Since(bT)
	log.Printf("run time: %v", eT)
}

func scanJavDir(scanDir string) int {
	codeFPathMap := make(map[string]string)
	scan.JavDic(scanDir, &codeFPathMap)
	count := 0
	for code := range codeFPathMap {
		log.Printf("%s has matched string: %v", code, codeFPathMap[code])
		jav := getJavCodeInfo(code)
		log.Printf("%+v", *jav)
		count++
		eleId := updateOrInsertJav(jav)
		log.Printf("eleId: %d", eleId)
		insertEleFile(codeFPathMap[code], eleId)
	}
	return count
}

func insertEleFile(path string, eleId int64) {
	eleFileQ := queries.EleFile
	name := path[strings.LastIndex(path, string(os.PathSeparator))+1:]
	eleFileType := getEleFileType(name)
	log.Printf("file name: %v", name)
	oldEleFiles, err := eleFileQ.Where(eleFileQ.PATH.Eq(path), eleFileQ.ELEID.Eq(eleId)).Find()
	if err != nil {
		log.Printf("db error:%v", err)
	}
	if len(oldEleFiles) == 0 {
		_ = eleFileQ.Create(&dao.EleFile{
			ELEID:           eleId,
			NAME:            name,
			TYPE:            eleFileType,
			PATH:            path,
			ISAVAILABLEFLAG: 1,
		})
	} else {
		log.Fatalf(" eleFile already exist...eleId: %v, path: %v", eleId, path)
	}

}

func getEleFileType(name string) string {
	eleFileType := "0"
	if strings.Contains(name, "mp4") {
		eleFileType = "2"
	} else if strings.Contains(name, "avi") {
		eleFileType = "3"
	} else {
		panic("type not found: " + name)
	}
	return eleFileType
}

func updateOrInsertJav(jav *jav) int64 {
	javQ := queries.Jav
	oldJavs, err := javQ.Where(javQ.TITLE.Eq(jav.title)).Find()
	if err != nil {
		log.Fatal("db exec failed, ", err)
	}
	if len(oldJavs) != 0 {
		return oldJavs[0].ELEID
	}

	tags := make([]dao.TagInfo, 0)
	for _, javTag := range jav.tags {
		tagQ := queries.TagInfo
		searchTag, _ := tagQ.Where(tagQ.NAME.Eq(javTag)).Find()
		if len(searchTag) == 0 {
			tags = append(tags, dao.TagInfo{
				NAME:      javTag,
				GROUPNAME: "",
				SOURCE:    Jav,
			})
		} else {
			tags = append(tags, *searchTag[0])
		}
	}

	actors := make([]dao.Actor, 0)
	for _, actor := range jav.actors {
		actorQ := queries.Actor
		searchRes, _ := actorQ.Where(actorQ.NAME.Eq(actor)).Find()
		if len(searchRes) == 0 {
			actors = append(actors, dao.Actor{
				NAME: actor,
			})
		} else {
			actors = append(actors, *searchRes[0])
		}
	}

	organs := make([]dao.Organization, 0)
	organQ := queries.Organization
	searchPro, _ := organQ.Where(organQ.NAME.Eq(jav.producer)).Find()
	if len(searchPro) == 0 {
		organs = append(organs, dao.Organization{
			NAME: jav.producer,
		})
	} else {
		organs = append(organs, *searchPro[0])
	}
	searchPro, _ = organQ.Where(organQ.NAME.Eq(jav.publisher)).Find()
	if len(searchPro) == 0 {
		organs = append(organs, dao.Organization{
			NAME: jav.publisher,
		})
	} else {
		organs = append(organs, *searchPro[0])
	}

	newEle := &dao.Element{
		TYPE:       Jav,
		SHAREDFLAG: 0,
		UPLOADER:   "root",
		EleFile: []dao.EleFile{{
			NAME:            "test",
			TYPE:            ".mp4",
			PATH:            "test/test.mp4",
			PAGENUM:         0,
			ISAVAILABLEFLAG: 1,
		}},
		Actor:        actors,
		Author:       nil,
		Organization: organs,
		TagInfo:      tags,
	}
	err = queries.Element.Create(newEle)
	if err != nil {
		log.Fatal("db exec failed, ", err)
	}
	return newEle.ID
}

func getJavCodeInfo(code string) *jav {

	doc, err := htmlquery.LoadURL(urlPrefix + code)
	if err != nil {
		log.Printf("parse failed, err:%v\n", err)
		log.Fatal("parse error, please check your network")
	}
	content := htmlquery.InnerText(doc)
	jav := jav{}
	if strings.Contains(content, "404 Page Not Found!") {
		jav.title = code
		jav.code = code
		jav.tags = []string{"unknown"}
		jav.publishDate = time.Now()
		return &jav
	}
	jav.title = htmlquery.InnerText(htmlquery.FindOne(doc, "/html/body/div[5]/h3"))
	p := htmlquery.FindOne(doc, "/html/body/div[5]/div[1]/div[2]")
	for c := p.FirstChild; c != nil; c = c.NextSibling {
		if c.Data != "p" {
			continue
		}
		content := removeSpace(htmlquery.InnerText(c))
		infos := strings.Split(content, ":")
		if len(infos) <= 1 {
			log.Fatal("unknown format, please contact the admin")
		}
		left := infos[0]
		right := infos[1]
		if len(strings.TrimSpace(right)) == 0 {
			if c == nil {
				break
			}
			for c = c.NextSibling; c != nil; c = c.NextSibling {
				if c.Data != "p" {
					continue
				}
				right = strings.TrimSpace(htmlquery.InnerText(c))
				if len(right) != 0 {
					break
				}
			}
		}

		//log.Printf("left: '%v', right: '%v'\n", left, right)
		if left == "識別碼" {
			jav.code = right
		}
		if left == "發行日期" {
			t, err := time.Parse(YYYYMMDD, right)
			if err != nil {
				log.Fatal(err)
			}
			jav.publishDate = t
		}
		if left == "長度" {
			jav.length = right
		}
		if left == "導演" {
			jav.director = right
		}
		if left == "製作商" {
			jav.producer = right
		}
		if left == "發行商" {
			jav.publisher = right
		}
		if left == "系列" {
			jav.series = right
		}
		if left == "類別" {
			right = strings.Replace(right, "多選提交", "", -1)
			jav.tags = strings.Fields(right)
		}
		if left == "演員" {
			jav.actors = strings.Fields(right)
		}
		if c == nil {
			break
		}
	}
	return &jav
}

func removeSpace(c string) string {
	content := spaceReg.ReplaceAllString(c, "")
	return content
}
func init() {
	// TODO config
	ormDb, err := gorm.Open(mysql.Open("root:12345678@tcp(127.0.0.1:3306)/partridge?charset=utf8mb4&parseTime=True&loc=Local"))
	if err != nil {
		log.Println("open mysql failed,", err)
		panic(fmt.Sprintf("invalid database %q", err))
	}
	queries = query.Use(ormDb)
	log.Println("open mysql succeed")
}

type jav struct {
	title       string
	code        string
	publishDate time.Time
	length      string
	director    string
	producer    string
	publisher   string
	series      string
	tags        []string
	actors      []string
}
