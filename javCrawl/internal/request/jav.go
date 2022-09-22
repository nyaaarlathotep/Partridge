package request

import (
	"github.com/antchfx/htmlquery"
	"log"
	"regexp"
	"strings"
	"time"
)

const YYYYMMDD = "2006-01-02"
const urlPrefix = "https://www.javbus.com/"

var spaceReg = regexp.MustCompile("\\s+")

func GetJavInfo(code string) *JavInfo {

	doc, err := htmlquery.LoadURL(urlPrefix + code)
	if err != nil {
		log.Printf("parse failed, err:%v\n", err)
		log.Fatal("parse error, please check your network")
	}
	content := htmlquery.InnerText(doc)
	jav := JavInfo{}
	if strings.Contains(content, "404 Page Not Found!") {
		jav.Title = code
		jav.Code = code
		jav.Tags = []string{"unknown"}
		jav.PublishDate = time.Now()
		return &jav
	}
	jav.Title = htmlquery.InnerText(htmlquery.FindOne(doc, "/html/body/div[5]/h3"))
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
			jav.Code = right
		}
		if left == "發行日期" {
			t, err := time.Parse(YYYYMMDD, right)
			if err != nil {
				log.Fatal(err)
			}
			jav.PublishDate = t
		}
		if left == "長度" {
			jav.Length = right
		}
		if left == "導演" {
			jav.Director = right
		}
		if left == "製作商" {
			jav.Producer = right
		}
		if left == "發行商" {
			jav.Publisher = right
		}
		if left == "系列" {
			jav.Series = right
		}
		if left == "類別" {
			right = strings.Replace(right, "多選提交", "", -1)
			jav.Tags = strings.Fields(right)
		}
		if left == "演員" {
			jav.Actors = strings.Fields(right)
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

type JavInfo struct {
	Title       string
	Code        string
	PublishDate time.Time
	Length      string
	Director    string
	Producer    string
	Publisher   string
	Series      string
	Tags        []string
	Actors      []string
}
