package main

import (
	"errors"
	"fmt"
	"github.com/antchfx/htmlquery"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
	"javCrawl/internal/scan"
	"log"
	"os"
	"regexp"
	"runtime"
	"strings"
	"time"
)

// TODO transactional support
// TODO or maybe multi thread
var db *sqlx.DB
var (
	NotFound = errors.New("sql: no rows in result set")
)

const urlPrefix = "https://www.javbus.com/"
const YYYYMMDD = "2006-01-02"
const javDir = "/media/nyaaar/bigbro/stream/movies"
const (
	Jav     string = "1"
	eHentai string = "2"
)

var spaceReg = regexp.MustCompile("\\s+")

func main() {
	bT := time.Now()
	defer func(db *sqlx.DB) {
		err := db.Close()
		if err != nil {
			log.Println(err)
		}
	}(db)
	count := scanJavDir(javDir)
	log.Printf("update or insert jav num: %v", count)
	log.Println(runtime.GOOS)
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

func insertEleFile(path string, eleId int) {
	insertEleFile := "INSERT INTO ele_file(ele_id,name,type,path,IS_AVAILABLE_FLAG ,CREATED_TIME, UPDATED_TIME) VALUES(?,?,?,?,?,?,?)"
	selectEleFIle := "select ID from ele_file where path=?"
	name := path[strings.LastIndex(path, string(os.PathSeparator))+1:]
	eleFileType := getEleFileType(name)

	log.Printf("file name: %v", name)
	var fileId = new(dbId)
	err := db.Get(fileId, selectEleFIle, path)
	_, err = db.Exec(insertEleFile, eleId, name, eleFileType, path, 1, time.Now(), time.Now())
	if err != nil {
		log.Fatal("db exec failed, ", err)
	}
}

func getEleFileType(name string) int {
	eleFileType := 0
	if strings.Contains(name, "mp4") {
		eleFileType = 2
	} else if strings.Contains(name, "avi") {
		eleFileType = 3
	} else {
		panic("type not found: " + name)
	}
	return eleFileType
}

func updateOrInsertJav(jav *jav) int {
	insertOrgReSql := "INSERT INTO ele_org_re(ELE_ID,ORG_ID,RE_TYPE) VALUES(?,?,?)"
	insertOrganSql := "INSERT INTO organization(name, CREATED_TIME, UPDATED_TIME) VALUES(?,?,?)"
	selectOrganSql := "select ID from organization where NAME=?"
	insertActorSql := "INSERT INTO actor(name, CREATED_TIME, UPDATED_TIME) VALUES(?,?,?)"
	insertTagSql := "INSERT INTO tag_info(name, CREATED_TIME, UPDATED_TIME) VALUES(?,?,?)"
	selectActorSql := "SELECT ID from actor where NAME=?"
	insertActorReSql := "INSERT INTO ele_actor_re(ELE_ID,ACTOR_ID) VALUES(?,?)"
	selectTagSql := "SELECT ID from tag_info where NAME=?"
	insertTagReSql := "INSERT INTO ele_tag_re(ELE_ID,TAG_ID) VALUES(?,?)"
	insertEleSql := "INSERT INTO element(TYPE, CREATED_TIME, UPDATED_TIME) VALUES(" + Jav + ", ?, ?)"
	insertJavSql := "CREATED_TIME, UPDATED_TIME) VALUES(?,?,?,?,?,?,?, ?, ?)" + "INSERT INTO jav(ELE_ID, CODE, TITLE, PUBLISH_DATE, LENGTH, DIRECTOR, SERIES, "
	selectJavSql := "SELECT ELE_ID AS ID from jav where TITLE=?"
	var eleId = new(dbId)
	err := db.Get(eleId, selectJavSql, jav.title)
	if err != nil && errors.As(err, &NotFound) {
		eleRes, err := db.Exec(insertEleSql, time.Now(), time.Now())
		if err != nil {
			log.Fatal("db exec failed, ", err)
		}
		newEleId, err := eleRes.LastInsertId()
		eleId.Id = int(newEleId)
		if err != nil {
			log.Fatal("db exec failed, ", err)
		}
		_, err = db.Exec(insertJavSql, newEleId, jav.code, jav.title, jav.publishDate, jav.length, jav.director, jav.series, time.Now(), time.Now())
		if err != nil {
			log.Fatal("db exec failed, ", err)
		}

		if len(jav.publisher) != 0 {
			var publisherId int64
			getOrInsertGetId(&publisherId, selectOrganSql, jav.publisher, insertOrganSql)
			insertOrgRe(insertOrgReSql, newEleId, publisherId, "publish")
		}

		if len(jav.producer) != 0 {
			var producerId int64
			getOrInsertGetId(&producerId, selectOrganSql, jav.producer, insertOrganSql)
			insertOrgRe(insertOrgReSql, newEleId, producerId, "produce")
		}
		if len(jav.actors) != 0 {
			for _, actor := range jav.actors {
				var actorId int64
				getOrInsertGetId(&actorId, selectActorSql, actor, insertActorSql)
				insertDoubleRe(insertActorReSql, int(newEleId), int(actorId))
			}

		}
		if len(jav.tags) != 0 {
			for _, tag_info := range jav.tags {
				var tagId int64
				getOrInsertGetId(&tagId, selectTagSql, tag_info, insertTagSql)
				insertDoubleRe(insertTagReSql, int(newEleId), int(tagId))
			}
		}
	} else if err != nil {
		log.Fatal("cannot find jav and error, why?", err)
	} else {
		// TODO: update
		log.Printf("update id: %v\n", -1)
	}
	log.Printf("eleId: %v", eleId)

	return eleId.Id
}

func insertDoubleRe(insertSql string, idOne int, idTwo int) {
	_, err := db.Exec(insertSql, idOne, idTwo)
	if err != nil {
		log.Fatal("db exec failed, ", err)
	}
}

func insertOrgRe(insertOrgRe string, eleId int64, organId int64, typeName string) {
	_, err := db.Exec(insertOrgRe, eleId, organId, typeName)
	if err != nil {
		log.Fatal("db exec failed, ", err)
	}
}

func getOrInsertGetId(dbId *int64, selectSql string, name string, insertSql string) {
	var getId int64
	err := db.Get(&getId, selectSql, name)
	if err != nil && errors.As(err, &NotFound) {
		log.Printf("%v not found, turn to insert...", name)
		insertRes, err := db.Exec(insertSql, name, time.Now(), time.Now())
		if err != nil {
			log.Fatal("db exec failed, ", err)
		}
		pbId, err := insertRes.LastInsertId()
		if err != nil {
			log.Fatal("db exec failed, ", err)
		}
		log.Printf("insert success! id: %v", pbId)
		*dbId = pbId
		return
	} else if err != nil {
		log.Fatal("db exec failed, ", err)
		return
	}
	*dbId = getId
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
	database, err := sqlx.Open("mysql", "root:12345678@tcp(127.0.0.1:3306)/partridge")
	if err != nil {
		log.Println("open mysql failed,", err)
		panic(fmt.Sprintf("invalid database %q", err))
	}
	time.Now()
	db = database
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

type dbId struct {
	Id int `db:"ID"`
}
