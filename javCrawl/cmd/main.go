package main

import (
	"fmt"
	"github.com/gin-gonic/gin"
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"javCrawl/internal/dal/dao"
	"javCrawl/internal/dal/query"
	"javCrawl/internal/request"
	"javCrawl/internal/scan"
	"log"
	"os"
	"regexp"
	"strconv"
	"strings"
	"time"
)

// TODO transactional support
// TODO or maybe multi thread
var queries *query.Query

const javDir = "/mnt/d/temp"
const (
	Jav     string = "1"
	eHentai string = "2"
)

var number = regexp.MustCompile(`\d+`)

func main() {
	r := gin.Default()
	err := r.SetTrustedProxies([]string{"127.0.0.1"})
	if err != nil {
		log.Fatalf("trustProxy error:%v", err)
		return
	}
	r.GET("/ping", func(c *gin.Context) {
		log.Printf("RemoteIP: %v", c.RemoteIP())
		c.JSON(200, gin.H{
			"message": "pong",
		})
	})
	r.POST("/scanJav", func(c *gin.Context) {
		var form scanDirForm
		if c.ShouldBind(&form) == nil {
			log.Printf("%v", form)
			var duration time.Duration
			var count int
			if len(form.Dir) != 0 {
				bT := time.Now()
				count = scanJavDir(javDir)
				log.Printf("update or insert jav num: %v", count)
				eT := time.Since(bT)
				log.Printf("run time: %v", eT)
				duration = eT
			}
			c.JSON(200, gin.H{
				"message":  "SUCCESS",
				"duration": fmt.Sprintf("%v", duration),
				"count":    count,
			})
		} else {
			c.JSON(400, gin.H{
				"message": "form error",
			})
		}
	})
	err = r.Run(":8090")
	if err != nil {
		return
	}
}

func scanJavDir(scanDir string) int {
	codeFPathMap := make(map[string]string)
	scan.JavDic(scanDir, &codeFPathMap)
	count := 0
	for code := range codeFPathMap {
		log.Printf("[%s] path: %v", code, codeFPathMap[code])
		jav := request.GetJavInfo(code)
		log.Printf("%+v", *jav)
		count++
		updateOrInsertEle(jav, codeFPathMap[code])
	}
	return count
}

func getEleFileType(name string) string {
	eleFileType := "0"
	if strings.Contains(name, "mp4") {
		eleFileType = "2"
	} else if strings.Contains(name, "avi") {
		eleFileType = "3"
	} else {
		log.Fatalf("type not found: %v", name)
	}
	return eleFileType
}

func updateOrInsertEle(jav *request.JavInfo, path string) int64 {
	javQ := queries.Jav
	oldJavs, err := javQ.Where(javQ.TITLE.Eq(jav.Title)).Find()
	if err != nil {
		log.Fatalf("db exec failed, %v", err)
	}
	if len(oldJavs) != 0 {
		return oldJavs[0].ELEID
	}

	tags := getTags(jav)
	actors := getActors(jav)
	organs := getOrgans(jav)
	eleFile := getEleFile(path, err)

	newEle := &dao.Element{
		TYPE:         Jav,
		SHAREDFLAG:   0,
		UPLOADER:     "root",
		EleFile:      []dao.EleFile{*eleFile},
		Actor:        actors,
		Author:       nil,
		Organization: organs,
		TagInfo:      tags,
	}
	err, length := getJavLength(jav.Length, err, newEle)
	if err != nil {
		log.Printf("parse jav length error: %v", err)
		length = 0
	}

	err = javQ.Create(&dao.Jav{
		ELEID:       newEle.ID,
		CODE:        jav.Code,
		TITLE:       jav.Title,
		PUBLISHDATE: jav.PublishDate,
		LENGTH:      length,
		DIRECTOR:    jav.Director,
		SERIES:      jav.Series,
	})
	if err != nil {
		log.Fatalf("create jav error: %v", err)
	}

	return newEle.ID
}

func getJavLength(stringLength string, err error, newEle *dao.Element) (error, int32) {
	err = queries.Element.Create(newEle)
	if err != nil {
		return err, 0
	}
	lengths := number.FindAllString(stringLength, -1)
	if len(lengths) != 1 {
		return fmt.Errorf("find multi number"), 0
	}
	length, err := strconv.ParseInt(lengths[0], 10, 32)
	if err != nil {
		return err, 0
	}
	return nil, int32(length)
}

func getEleFile(path string, err error) *dao.EleFile {
	eleFileQ := queries.EleFile
	name := path[strings.LastIndex(path, string(os.PathSeparator))+1:]
	eleFileType := getEleFileType(name)
	log.Printf("file name: %v", name)
	oldEleFiles, err := eleFileQ.Where(eleFileQ.PATH.Eq(path)).Find()
	if err != nil {
		log.Printf("eleFile search db error:%v", err)
	}
	var eleFile *dao.EleFile
	if len(oldEleFiles) == 0 {
		eleFile = &dao.EleFile{
			NAME:            name,
			TYPE:            eleFileType,
			PATH:            path,
			ISAVAILABLEFLAG: 1,
		}
	} else {
		eleFile = oldEleFiles[0]
	}
	return eleFile
}

func getOrgans(jav *request.JavInfo) []dao.Organization {
	organs := make([]dao.Organization, 0)
	organQ := queries.Organization
	searchPro, _ := organQ.Where(organQ.NAME.Eq(jav.Producer), organQ.TYPE.Eq("producer")).Find()
	if len(searchPro) == 0 {
		organs = append(organs, dao.Organization{
			NAME: jav.Producer,
			TYPE: "producer",
		})
	} else {
		organs = append(organs, *searchPro[0])
	}
	searchPro, _ = organQ.Where(organQ.NAME.Eq(jav.Publisher), organQ.TYPE.Eq("publisher")).Find()
	if len(searchPro) == 0 {
		organs = append(organs, dao.Organization{
			NAME: jav.Publisher,
			TYPE: "publisher",
		})
	} else {
		organs = append(organs, *searchPro[0])
	}
	return organs
}

func getActors(jav *request.JavInfo) []dao.Actor {
	actors := make([]dao.Actor, 0)
	for _, actor := range jav.Actors {
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
	return actors
}

func getTags(jav *request.JavInfo) []dao.TagInfo {
	tags := make([]dao.TagInfo, 0)
	for _, javTag := range jav.Tags {
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
	return tags
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

type scanDirForm struct {
	Dir string `form:"dir" binding:"required"`
}
