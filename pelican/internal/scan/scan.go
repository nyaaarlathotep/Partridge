package scan

import (
	"io/ioutil"
	"javCrawl/internal/util"
	"log"
	"os"
	"strings"
)

func JavDic(dir string, codeFPathMap *map[string]string) {
	log.Printf("start to scan %v...", dir)
	files, err := ioutil.ReadDir(dir)
	if err != nil {
		log.Printf("read dir error, dir: %v", dir)
		log.Fatal(err)
	}
	smallCodeFPathMap := make(map[string]string)
	for _, file := range files {
		var builder strings.Builder
		builder.WriteString(dir)
		builder.WriteRune(os.PathSeparator)
		builder.WriteString(file.Name())
		completePath := builder.String()
		if file.IsDir() {
			JavDic(completePath, codeFPathMap)
		}
		if !util.HasJavRegex(file.Name()) {
			log.Printf("no match for name: %v", file.Name())
			continue
		}
		code := util.FormatJavCode(file.Name())
		smallCodeFPathMap[code] = completePath
	}
	if len(smallCodeFPathMap) < 1 {
		log.Printf("match error! file path:%s", dir)
	}
	for code := range smallCodeFPathMap {
		(*codeFPathMap)[code] = smallCodeFPathMap[code]
	}
}
