package service

import (
	"bytes"
	"fmt"
	"github.com/gin-gonic/gin"
	"io"
	"log"
	"os"
	"strconv"
	"strings"
)

const BOUNDARY string = "; boundary="

func UploadFile(c *gin.Context) error {
	var contentLength int64
	contentLength = c.Request.ContentLength
	if contentLength <= 0 || contentLength > 1024*1024*1024*2 {
		log.Printf("content_length error\n")
		return fmt.Errorf("content_length error")
	}
	contentType_, hasKey := c.Request.Header["Content-Type"]
	if !hasKey {
		log.Printf("Content-Type error\n")
		return fmt.Errorf("Content-Type error")
	}
	if len(contentType_) != 1 {
		log.Printf("Content-Type count error\n")
		return fmt.Errorf("Content-Type count error")
	}
	contentType := contentType_[0]
	loc := strings.Index(contentType, BOUNDARY)
	if -1 == loc {
		log.Printf("Content-Type error, no boundary\n")
		return fmt.Errorf("Content-Type error, no boundary")
	}
	boundary := []byte(contentType[(loc + len(BOUNDARY)):])
	log.Printf("[%s]\n\n", boundary)
	readData := make([]byte, 1024*12)
	var readTotal = 0
	for {
		fileHeader, fileData, err := ParseFromHead(readData, readTotal, append(boundary, []byte("\r\n")...), c.Request.Body)
		if err != nil {
			log.Printf("%v", err)
			return fmt.Errorf("%v", err)

		}
		log.Printf("file :%s\n", fileHeader.FileName)
		//
		f, err := os.Create(fileHeader.FileName)
		if err != nil {
			log.Printf("create file fail:%v\n", err)
			return fmt.Errorf("create file fail: %v", err)
		}
		f.Write(fileData)
		fileData = nil

		// Need to search for Boundary
		tempData, reachEnd, err := ReadToBoundary(boundary, c.Request.Body, f)
		f.Close()
		if err != nil {
			log.Printf("%v\n", err)
			return fmt.Errorf("%v", err)
		}
		if reachEnd {
			break
		} else {
			copy(readData[0:], tempData)
			readTotal = len(tempData)
			continue
		}
	}
	return nil
}

// ParseFileHeader / Analyze the header of the descriptor file information
/// @Return FileHeader file name and other information
/// @RETURN BOOL parses success or fail
func ParseFileHeader(h []byte) (FileHeader, bool) {
	arr := bytes.Split(h, []byte("\r\n"))
	var outHeader FileHeader
	outHeader.ContentLength = -1
	const (
		ContentDisposition = "Content-Disposition: "
		Name               = "name=\""
		FileName           = "filename=\""
		ContentType        = "Content-Type: "
		ContentLength      = "Content-Length: "
	)
	for _, item := range arr {
		if bytes.HasPrefix(item, []byte(ContentDisposition)) {
			l := len(ContentDisposition)
			arr1 := bytes.Split(item[l:], []byte("; "))
			outHeader.ContentDisposition = string(arr1[0])
			if bytes.HasPrefix(arr1[1], []byte(Name)) {
				outHeader.Name = string(arr1[1][len(Name) : len(arr1[1])-1])
			}
			l = len(arr1[2])
			if bytes.HasPrefix(arr1[2], []byte(FileName)) && arr1[2][l-1] == 0x22 {
				outHeader.FileName = string(arr1[2][len(FileName) : l-1])
			}
		} else if bytes.HasPrefix(item, []byte(ContentType)) {
			l := len(ContentType)
			outHeader.ContentType = string(item[l:])
		} else if bytes.HasPrefix(item, []byte(ContentLength)) {
			l := len(ContentLength)
			s := string(item[l:])
			contentLength, err := strconv.ParseInt(s, 10, 64)
			if err != nil {
				log.Printf("content length error:%s", string(item))
				return outHeader, false
			} else {
				outHeader.ContentLength = contentLength
			}
		} else {
			log.Printf("unknown:%s\n", string(item))
		}
	}
	if len(outHeader.FileName) == 0 {
		return outHeader, false
	}
	return outHeader, true
}

// ReadToBoundary From the stream to the last place of the file
/// @return [] BYTE does not write the file and belongs to the next file
//@Return BOOL has been read at the end of the stream
//@Return error does an error occurred
func ReadToBoundary(boundary []byte, stream io.ReadCloser, target io.WriteCloser) ([]byte, bool, error) {
	readData := make([]byte, 1024*8)
	readDataLen := 0
	buf := make([]byte, 1024*4)
	bLen := len(boundary)
	reachEnd := false
	for !reachEnd {
		readLen, err := stream.Read(buf)
		if err != nil {
			if err != io.EOF && readLen <= 0 {
				return nil, true, err
			}
			reachEnd = true
		}
		// TODO: The following is stupid, worth optimization
		copy(readData[readDataLen:], buf[:readLen]) // append to another buffer, just to search for convenience
		readDataLen += readLen
		if readDataLen < bLen+4 {
			continue
		}
		loc := bytes.Index(readData[:readDataLen], boundary)
		if loc >= 0 {
			// Find the end position
			target.Write(readData[:loc-4])
			return readData[loc:readDataLen], reachEnd, nil
		}

		target.Write(readData[:readDataLen-bLen-4])
		copy(readData[0:], readData[readDataLen-bLen-4:])
		readDataLen = bLen + 4
	}
	target.Write(readData[:readDataLen])
	return nil, reachEnd, nil
}

// ParseFromHead Analyze the head of the form
/// @Param read_data has been read from the stream
/// @Param Read_Total has been read from the stream
/// @Param Boundary form split string
/// @Param Stream input stream
// @Return FileHeader file name and other information
/// [] Byte has been read from the stream
/// error does an error have occurred
func ParseFromHead(readData []byte, readTotal int, boundary []byte, stream io.ReadCloser) (FileHeader, []byte, error) {
	buf := make([]byte, 1024*4)
	foundBoundary := false
	boundaryLoc := -1
	var fileHeader FileHeader
	for {
		readLen, err := stream.Read(buf)
		if err != nil {
			if err != io.EOF {
				return fileHeader, nil, err
			}
			break
		}
		if readTotal+readLen > cap(readData) {
			return fileHeader, nil, fmt.Errorf("not found boundary")
		}
		copy(readData[readTotal:], buf[:readLen])
		readTotal += readLen
		if !foundBoundary {
			boundaryLoc = bytes.Index(readData[:readTotal], boundary)
			if -1 == boundaryLoc {
				continue
			}
			foundBoundary = true
		}
		startLoc := boundaryLoc + len(boundary)
		fileHeadLoc := bytes.Index(readData[startLoc:readTotal], []byte("\r\n\r\n"))
		if -1 == fileHeadLoc {
			continue
		}
		fileHeadLoc += startLoc
		ret := false
		fileHeader, ret = ParseFileHeader(readData[startLoc:fileHeadLoc])
		if !ret {
			return fileHeader, nil, fmt.Errorf("ParseFileHeader fail:%s", string(readData[startLoc:fileHeadLoc]))
		}
		return fileHeader, readData[fileHeadLoc+4 : readTotal], nil
	}
	return fileHeader, nil, fmt.Errorf("reach to sream EOF")
}

type FileHeader struct {
	ContentDisposition string
	Name               string
	FileName           string // <file name
	ContentType        string
	ContentLength      int64
}
