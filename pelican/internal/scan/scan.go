package scan

import (
	"bufio"
	"fmt"
	"io/fs"
	"io/ioutil"
	"javCrawl/internal/util"
	"log"
	"os"
	"path/filepath"
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

//     public static SpiderInfo read(@Nullable InputStream is) {
//        if (null == is) {
//            return null;
//        }
//
//        SpiderInfo spiderInfo = null;
//        try {
//            spiderInfo = new SpiderInfo();
//            // Get version
//            String line = IOUtils.readAsciiLine(is);
//            int version = getVersion(line);
//            if (version == VERSION) {
//                // Read next line
//                line = IOUtils.readAsciiLine(is);
//            } else if (version == 1) {
//                // pass
//            } else {
//                // Invalid version
//                return null;
//            }
//            // Start page
//            spiderInfo.startPage = getStartPage(line);
//            // Gid
//            spiderInfo.gid = Long.parseLong(IOUtils.readAsciiLine(is));
//            // Token
//            spiderInfo.token = IOUtils.readAsciiLine(is);
//            // Deprecated, mode, skip it
//            IOUtils.readAsciiLine(is);
//            // Preview pages
//            spiderInfo.previewPages = Integer.parseInt(IOUtils.readAsciiLine(is));
//            // Preview pre page
//            line = IOUtils.readAsciiLine(is);
//            if (version == 1) {
//                // Skip it
//            } else {
//                spiderInfo.previewPerPage = Integer.parseInt(line);
//            }
//            // Pages
//            spiderInfo.pages = Integer.parseInt(IOUtils.readAsciiLine(is));
//            // Check pages
//            if (spiderInfo.pages <= 0) {
//                return null;
//            }
//            // PToken
//            spiderInfo.pTokenMap = new SparseArray<>(spiderInfo.pages);
//            while (true) { // EOFException will raise
//                line = IOUtils.readAsciiLine(is);
//                int pos = line.indexOf(" ");
//                if (pos > 0) {
//                    int index = Integer.parseInt(line.substring(0, pos));
//                    String pToken = line.substring(pos + 1);
//                    if (!TextUtils.isEmpty(pToken)) {
//                        spiderInfo.pTokenMap.put(index, pToken);
//                    }
//                } else {
//                    Log.e(TAG, "Can't parse index and pToken, index = " + pos);
//                }
//            }
//        } catch (IOException | NumberFormatException e) {
//            // Ignore
//        }
//
//        if (spiderInfo == null || spiderInfo.gid == -1 || spiderInfo.token == null ||
//                spiderInfo.pages == -1 || spiderInfo.pTokenMap == null) {
//            return null;
//        } else {
//            return spiderInfo;
//        }
//    }
func ehentaiScan(dir string) {
	folders, _ := ioutil.ReadDir(dir)
	for _, galleryFolder := range folders {
		log.Printf("galleryFolder name: %s", galleryFolder.Name())
		gid, name, err := util.GetGidAndName(galleryFolder.Name())
		log.Printf("[%v] name: %v", gid, name)
		if err != nil {
			log.Printf("read dir error, dir: %v", dir)
			log.Fatal(err)
		}
		if galleryFolder.IsDir() {
			scanGallery(dir, galleryFolder, gid)
		}

	}

}

func scanGallery(dir string, galleryFolder fs.FileInfo, gid string) {
	files, err := ioutil.ReadDir(filepath.Join(dir, galleryFolder.Name()))
	if err != nil {
		log.Printf("read dir error, dir: %v", dir)
		log.Fatal(err)
	}
	for _, file := range files {
		if strings.Contains(file.Name(), ".ehviewer") {
			gToken, err := readMetaData(filepath.Join(dir, galleryFolder.Name(), file.Name()), gid)
			log.Printf("[%v] gtoken: %v", gid, gToken)
			if err != nil {
				log.Printf("error on reading metadata")
				return
			}
		}
		// TODO create eleFile and call partridge to finish gallery info
	}
}

func readMetaData(path string, gid string) (string, error) {

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
	if line != gid {
		return "", fmt.Errorf("meta data mismitch, gid in file: %v", line)
	}
	line, err = r.ReadString('\n')
	line, err = r.ReadString('\n')
	return line, nil
}
