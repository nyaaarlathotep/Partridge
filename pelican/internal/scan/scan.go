package scan

import (
	"bufio"
	"fmt"
	"io/fs"
	"io/ioutil"
	"javCrawl/internal/dal/dao"
	"javCrawl/internal/util"
	"log"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"time"
)

const (
	Jav     string = "1"
	eHentai string = "2"
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

func EhentaiScan(dir string) []*dao.Element {
	folders, _ := ioutil.ReadDir(dir)
	elements := make([]*dao.Element, 0)
	for _, galleryFolder := range folders {
		log.Printf("galleryFolder name: %s", galleryFolder.Name())
		gid, name, err := util.GetGidAndName(galleryFolder.Name())
		log.Printf("[%v] name: %v", gid, name)
		if err != nil {
			log.Printf("read dir error, dir: %v", dir)
			log.Fatal(err)
		}
		if galleryFolder.IsDir() {
			elements = append(elements, scanGallery(dir, galleryFolder, gid))
		}
	}
	return elements
}

func scanGallery(dir string, galleryFolder fs.FileInfo, gid int64) *dao.Element {
	files, err := ioutil.ReadDir(filepath.Join(dir, galleryFolder.Name()))
	if err != nil {
		log.Printf("read dir error, dir: %v", dir)
		log.Fatal(err)
	}
	var gToken string
	ele := dao.Element{
		TYPE:          eHentai,
		FILEDIR:       filepath.Join(dir, galleryFolder.Name()),
		SHAREDFLAG:    1,
		PUBLISHEDFLAG: 1,
		EleFile:       []dao.EleFile{},
		UPLOADER:      "root",
		AVAILABLEFLAG: 1,
	}
	for _, file := range files {
		if strings.Contains(file.Name(), ".ehviewer") {
			gToken, err = readMetaData(filepath.Join(dir, galleryFolder.Name(), file.Name()), gid)
			if err != nil {
				log.Fatalf("error on reading metadata, e: %v", err)
			}
			log.Printf("[%v] gtoken: %v", gid, gToken)
			continue
		}
		err, pageIndex := util.GetNumFromString(file.Name())
		if err != nil {
			log.Printf("parse page index error, e: %v", err)
			continue
		}
		ele.EleFile = append(ele.EleFile, dao.EleFile{
			NAME:          file.Name(),
			TYPE:          eHentai,
			PATH:          filepath.Join(dir, galleryFolder.Name(), file.Name()),
			PAGENUM:       int32(pageIndex),
			COMPLETEDFLAG: 1,
			AVAILABLEFLAG: 1,
		})
	}
	ele.Ehentai_gallery = dao.EhentaiGallery{
		GID:    gid,
		TOKEN:  gToken,
		POSTED: time.Now(),
	}
	return &ele

}

func readMetaData(path string, gid int64) (string, error) {

	f, err := os.Open(path)
	if err != nil {
		log.Printf("read file error, name: %v", f.Name())
		return "", fmt.Errorf("read file error, name: %v", f.Name())
	}
	defer func(f *os.File) {
		err := f.Close()
		if err != nil {
			log.Printf("close dir error, name: %v", f.Name())
		}
	}(f)
	var line string
	r := bufio.NewReader(f)
	line, err = r.ReadString('\n')
	if strings.Contains(line, "VERSION2") {
		line, err = r.ReadString('\n')
	}
	line, err = r.ReadString('\n')
	line = strings.Replace(line, "\n", "", -1)
	if line != strconv.FormatInt(gid, 10) {
		return "", fmt.Errorf("meta data mismitch, gid in file: %v, gid: %v",
			line, strconv.FormatInt(gid, 10))
	}
	line, err = r.ReadString('\n')
	line, err = r.ReadString('\n')
	return strings.Replace(line, "\n", "", -1), nil
}
