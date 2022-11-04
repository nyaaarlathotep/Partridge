package util

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"
)

var number = regexp.MustCompile(`\d+`)
var movieReg = regexp.MustCompile(".*[a-zA-Z]{3,5}[-_]?[0-9]{3}.*(avi|mp4)$")
var codeReg = regexp.MustCompile("[a-zA-Z]{3,5}[-_]?[0-9]{3}")
var bigLetter = regexp.MustCompile("[A-Z]+")
var ehvFolder = regexp.MustCompile(`\d+-.*`)

func GetNumFromString(stringLength string) (error, int64) {
	lengths := number.FindAllString(stringLength, -1)
	if len(lengths) != 1 {
		return fmt.Errorf("find multi number"), 0
	}
	length, err := strconv.ParseInt(lengths[0], 10, 64)
	if err != nil {
		return err, 0
	}
	return nil, length
}

func HasJavRegex(name string) bool {
	matches := movieReg.FindAllString(name, -1)
	return len(matches) != 0
}

func FormatJavCode(rawCode string) string {
	matches := codeReg.FindAllString(rawCode, -1)
	code := matches[len(matches)-1]
	code = strings.ToUpper(code)
	code = strings.Replace(code, "_", "-", -1)
	if !strings.ContainsRune(code, '-') {
		index := bigLetter.FindAllStringIndex(code, 1)
		code = code[0:index[0][1]] + "-" + code[index[0][1]:]
	}
	return code
}

func GetGidAndName(folderName string) (string, string, error) {
	matches := ehvFolder.FindAllString(folderName, -1)
	if len(matches) == 0 {
		return "", "", fmt.Errorf("folder name parse error, folder name: [%s]", folderName)
	}
	parts := strings.Split(folderName, "-")
	return parts[0], parts[1], nil
}
