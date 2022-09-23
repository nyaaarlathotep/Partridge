// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.

package dao

import (
	"time"
)

const TableNameTagInfo = "tag_info"

// TagInfo mapped from table <tag_info>
type TagInfo struct {
	ID        int32     `gorm:"column:ID;type:int;primaryKey;autoIncrement:true" json:"ID"`
	NAME      string    `gorm:"column:NAME;type:varchar(255);not null" json:"NAME"`
	GROUPNAME string    `gorm:"column:GROUP_NAME;type:varchar(255)" json:"GROUP_NAME"`
	SOURCE    string    `gorm:"column:SOURCE;type:varchar(255)" json:"SOURCE"`
	CreatedAt time.Time `gorm:"column:CREATED_TIME;type:datetime" json:"CREATED_TIME"`
	UpdatedAt time.Time `gorm:"column:UPDATED_TIME;type:datetime" json:"UPDATED_TIME"`
}

// TableName TagInfo's table name
func (*TagInfo) TableName() string {
	return TableNameTagInfo
}