// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.

package dao

import (
	"time"
)

const TableNameEleFile = "ele_file"

// EleFile mapped from table <ele_file>
type EleFile struct {
	ID            int64     `gorm:"column:ID;type:bigint;primaryKey;autoIncrement:true" json:"ID"`
	ELEID         int64     `gorm:"column:ELE_ID;type:bigint" json:"ELE_ID"`
	NAME          string    `gorm:"column:NAME;type:varchar(255)" json:"NAME"`
	TYPE          string    `gorm:"column:TYPE;type:varchar(32)" json:"TYPE"`
	PATH          string    `gorm:"column:PATH;type:varchar(255)" json:"PATH"`
	PAGENUM       int32     `gorm:"column:PAGE_NUM;type:tinyint" json:"PAGE_NUM"`             // ehentai_gallery 对应画廊文件页码
	COMPLETEDFLAG int32     `gorm:"column:COMPLETED_FLAG;type:tinyint" json:"COMPLETED_FLAG"` // 完成标志(0-禁用;1-启用)，可以被删除
	AVAILABLEFLAG int32     `gorm:"column:AVAILABLE_FLAG;type:tinyint" json:"AVAILABLE_FLAG"` // 启用标志(0-禁用;1-启用)
	CreatedAt     time.Time `gorm:"column:CREATED_TIME;type:datetime" json:"CREATED_TIME"`
	UpdatedAt     time.Time `gorm:"column:UPDATED_TIME;type:datetime" json:"UPDATED_TIME"`
}

// TableName EleFile's table name
func (*EleFile) TableName() string {
	return TableNameEleFile
}
