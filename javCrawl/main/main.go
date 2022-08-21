package main

import (
	"fmt"
	"github.com/antchfx/htmlquery"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
	"log"
	"regexp"
	"strings"
	"time"
)

var Db *sqlx.DB

const root = "/media/disk1/file/music/neteaseDownloader/"
const urlPrefix = "https://www.javbus.com/"
const YYYYMMDD = "2006-01-02"

func main() {
	defer func(Db *sqlx.DB) {
		err := Db.Close()
		if err != nil {
			log.Println(err)
		}
	}(Db)

	jav := getJavCodeInfo("MVG-032")
	log.Printf("%+v", *jav)
}

func getJavCodeInfo(code string) *jav {

	doc, err := htmlquery.LoadURL(urlPrefix + code)
	if err != nil {
		log.Printf("parse failed, err:%v\n", err)
		log.Fatal("parse error, please check your network")
	}
	p := htmlquery.FindOne(doc, "/html/body/div[5]/div[1]/div[2]")
	jav := jav{}
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
			jav.releaseDate = t
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
		if left == "類別" {
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
	Db = database
	log.Println("open mysql succeed")
}

type jav struct {
	code        string
	releaseDate time.Time
	length      string
	director    string
	producer    string
	publisher   string
	series      string
	tags        []string
	actors      []string
}
