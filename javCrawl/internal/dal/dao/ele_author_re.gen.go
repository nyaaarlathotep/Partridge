// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.

package dao

const TableNameEleAuthorRe = "ele_author_re"

// EleAuthorRe mapped from table <ele_author_re>
type EleAuthorRe struct {
	ID       int32 `gorm:"column:ID;type:int;primaryKey;autoIncrement:true" json:"ID"`
	ELEID    int64 `gorm:"column:ELE_ID;type:bigint;not null" json:"ELE_ID"`
	AUTHORID int32 `gorm:"column:AUTHOR_ID;type:int;not null" json:"AUTHOR_ID"`
}

// TableName EleAuthorRe's table name
func (*EleAuthorRe) TableName() string {
	return TableNameEleAuthorRe
}