package scan

import (
	"io/ioutil"
	"log"
	"os"
	"regexp"
	"strings"
)

var movieReg = regexp.MustCompile(".*[a-zA-Z]{3,5}[-_]?[0-9]{3}.*(avi|mp4)$")
var codeReg = regexp.MustCompile("[a-zA-Z]{3,5}[-_]?[0-9]{3}")
var bigLetter = regexp.MustCompile("[A-Z]+")

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
		matches := movieReg.FindAllString(file.Name(), -1)
		if len(matches) == 0 {
			log.Printf("no match for name: %v", file.Name())
			continue
		}
		code := getAndFormatCode(matches, file.Name())
		smallCodeFPathMap[code] = completePath
	}
	if len(smallCodeFPathMap) < 1 {
		log.Printf("match error! file path:%s", dir)
	}
	for code := range smallCodeFPathMap {
		(*codeFPathMap)[code] = smallCodeFPathMap[code]
	}
}

func getAndFormatCode(matches []string, fileName string) string {
	matches = codeReg.FindAllString(fileName, -1)
	code := matches[len(matches)-1]
	code = strings.ToUpper(code)
	if !strings.ContainsRune(code, '-') {
		index := bigLetter.FindAllStringIndex(code, 1)
		code = code[0:index[0][1]] + "-" + code[index[0][1]:]
	}
	return code
}
