package main

import (
	"errors"
	"fmt"
	"github.com/antchfx/htmlquery"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
	"log"
	"regexp"
	"strings"
	"time"
)

var db *sqlx.DB
var (
	NotFound = errors.New("sql: no rows in result set")
)

const urlPrefix = "https://www.javbus.com/"
const YYYYMMDD = "2006-01-02"

func main() {
	defer func(db *sqlx.DB) {
		err := db.Close()
		if err != nil {
			log.Println(err)
		}
	}(db)

	jav := getJavCodeInfo("MVG-032")
	log.Printf("%+v", *jav)
	updateOrInsertJav(jav)

}

func updateOrInsertJav(jav *jav) {
	insertOrgReSql := "INSERT INTO ele_org_re(ELE_ID,ORG_ID,RE_TYPE) VALUES(?,?,?)"
	insertOrganSql := "INSERT INTO organization(name) VALUES(?)"
	selectOrganSql := "select ID from organization where NAME=?"
	insertActorSql := "INSERT INTO actor(name) VALUES(?)"
	insertTagSql := "INSERT INTO tag(name) VALUES(?)"
	selectActorSql := "SELECT ID from actor where NAME=?"
	insertActorReSql := "INSERT INTO ele_actor_re(ELE_ID,ACTOR_ID) VALUES(?,?)"
	selectTagSql := "SELECT ID from tag where NAME=?"
	insertTagReSql := "INSERT INTO ele_tag_re(ELE_ID,TAG_ID) VALUES(?,?)"
	insertEleSql := "INSERT INTO element(TYPE, RECORDED_TIME) VALUES('jav',?)"
	insertJavSql := "INSERT INTO jav(ELE_ID, CODE, TITLE, PUBLISH_DATE, LENGTH, DIRECTOR, SERIES) VALUES(?,?,?,?,?,?,?)"
	selectJavSql := "SELECT ELE_ID from jav where TITLE=?"
	var eleId dbId
	err := db.Get(&eleId, selectJavSql, jav.title)
	if err != nil && errors.As(err, &NotFound) {
		eleRes, err := db.Exec(insertEleSql, time.Now())
		if err != nil {
			log.Fatal("exec failed, ", err)
		}
		eleId, err := eleRes.LastInsertId()
		if err != nil {
			log.Fatal("exec failed, ", err)
		}
		_, err = db.Exec(insertJavSql, eleId, jav.code, jav.title, jav.publishDate, jav.length, jav.director, jav.series)
		if err != nil {
			log.Fatal("exec failed, ", err)
		}

		if len(jav.publisher) != 0 {
			var publisherId int64
			getOrInsertGetId(&publisherId, selectOrganSql, jav.publisher, insertOrganSql)
			insertOrgRe(insertOrgReSql, eleId, publisherId, "publish")
		}

		if len(jav.producer) != 0 {
			var producerId int64
			getOrInsertGetId(&producerId, selectOrganSql, jav.producer, insertOrganSql)
			insertOrgRe(insertOrgReSql, eleId, producerId, "produce")
		}
		if len(jav.actors) != 0 {
			for _, actor := range jav.actors {
				var actorId int64
				getOrInsertGetId(&actorId, selectActorSql, actor, insertActorSql)
				insertDoubleRe(insertActorReSql, int(eleId), int(actorId))
			}

		}
		if len(jav.tags) != 0 {
			for _, tag := range jav.tags {
				var tagId int64
				getOrInsertGetId(&tagId, selectTagSql, tag, insertTagSql)
				log.Printf("tag: %v", tagId)
				insertDoubleRe(insertTagReSql, int(eleId), int(tagId))
			}
		}
	}
	// TODO: update
	log.Printf("update id: %v\n", eleId.id)
}

func insertDoubleRe(insertSql string, idOne int, idTwo int) {
	_, err := db.Exec(insertSql, idOne, idTwo)
	if err != nil {
		log.Fatal("exec failed, ", err)
	}
}

func insertOrgRe(insertOrgRe string, eleId int64, organId int64, typeName string) {
	_, err := db.Exec(insertOrgRe, eleId, organId, typeName)
	if err != nil {
		log.Fatal("exec failed, ", err)
	}
}

func getOrInsertGetId(dbId *int64, selectSql string, name string, insert string) {
	var getId int64
	err := db.Get(&getId, selectSql, name)
	if err != nil && errors.As(err, &NotFound) {
		log.Printf("%v not found, turn to insert...", name)
		pbRes, err := db.Exec(insert, name)
		if err != nil {
			log.Fatal("exec failed, ", err)
		}
		pbId, err := pbRes.LastInsertId()
		if err != nil {
			log.Fatal("exec failed, ", err)
		}
		log.Printf("insert success! id: %v", pbId)
		*dbId = pbId
		return
	} else if err != nil {
		log.Fatal("exec failed, ", err)
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
	jav := jav{}
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
	reg := regexp.MustCompile("\\s+")
	content := reg.ReplaceAllString(c, "")
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
	id int `db:"ID"`
}
