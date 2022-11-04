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

}
