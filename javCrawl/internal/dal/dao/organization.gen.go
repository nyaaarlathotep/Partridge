// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.

package dao

import (
	"time"
)

const TableNameOrganization = "organization"

// Organization mapped from table <organization>
type Organization struct {
	ID        int32     `gorm:"column:ID;type:int;primaryKey;autoIncrement:true" json:"ID"`
	NAME      string    `gorm:"column:NAME;type:varchar(255)" json:"NAME"`
	TYPE      string    `gorm:"column:TYPE;type:varchar(64)" json:"TYPE"` // 发行商，制作商
	CreatedAt time.Time `gorm:"column:CREATED_TIME;type:datetime" json:"CREATED_TIME"`
	UpdatedAt time.Time `gorm:"column:UPDATED_TIME;type:datetime" json:"UPDATED_TIME"`
}

// TableName Organization's table name
func (*Organization) TableName() string {
	return TableNameOrganization
}
