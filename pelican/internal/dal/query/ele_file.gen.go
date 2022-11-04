// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.

package query

import (
	"context"

	"gorm.io/gorm"
	"gorm.io/gorm/clause"
	"gorm.io/gorm/schema"

	"gorm.io/gen"
	"gorm.io/gen/field"

	"gorm.io/plugin/dbresolver"

	"javCrawl/internal/dal/dao"
)

func newEleFile(db *gorm.DB) eleFile {
	_eleFile := eleFile{}

	_eleFile.eleFileDo.UseDB(db)
	_eleFile.eleFileDo.UseModel(&dao.EleFile{})

	tableName := _eleFile.eleFileDo.TableName()
	_eleFile.ALL = field.NewAsterisk(tableName)
	_eleFile.ID = field.NewInt64(tableName, "ID")
	_eleFile.ELEID = field.NewInt64(tableName, "ELE_ID")
	_eleFile.NAME = field.NewString(tableName, "NAME")
	_eleFile.TYPE = field.NewString(tableName, "TYPE")
	_eleFile.PATH = field.NewString(tableName, "PATH")
	_eleFile.PAGENUM = field.NewInt32(tableName, "PAGE_NUM")
	_eleFile.COMPLETEDFLAG = field.NewInt32(tableName, "COMPLETED_FLAG")
	_eleFile.AVAILABLEFLAG = field.NewInt32(tableName, "AVAILABLE_FLAG")
	_eleFile.CreatedAt = field.NewTime(tableName, "CREATED_TIME")
	_eleFile.UpdatedAt = field.NewTime(tableName, "UPDATED_TIME")

	_eleFile.fillFieldMap()

	return _eleFile
}

type eleFile struct {
	eleFileDo

	ALL           field.Asterisk
	ID            field.Int64
	ELEID         field.Int64
	NAME          field.String
	TYPE          field.String
	PATH          field.String
	PAGENUM       field.Int32 // ehentai_gallery 对应画廊文件页码
	COMPLETEDFLAG field.Int32 // 完成标志(0-禁用;1-启用)
	AVAILABLEFLAG field.Int32 // 启用标志(0-禁用;1-启用)
	CreatedAt     field.Time
	UpdatedAt     field.Time

	fieldMap map[string]field.Expr
}

func (e eleFile) Table(newTableName string) *eleFile {
	e.eleFileDo.UseTable(newTableName)
	return e.updateTableName(newTableName)
}

func (e eleFile) As(alias string) *eleFile {
	e.eleFileDo.DO = *(e.eleFileDo.As(alias).(*gen.DO))
	return e.updateTableName(alias)
}

func (e *eleFile) updateTableName(table string) *eleFile {
	e.ALL = field.NewAsterisk(table)
	e.ID = field.NewInt64(table, "ID")
	e.ELEID = field.NewInt64(table, "ELE_ID")
	e.NAME = field.NewString(table, "NAME")
	e.TYPE = field.NewString(table, "TYPE")
	e.PATH = field.NewString(table, "PATH")
	e.PAGENUM = field.NewInt32(table, "PAGE_NUM")
	e.COMPLETEDFLAG = field.NewInt32(table, "COMPLETED_FLAG")
	e.AVAILABLEFLAG = field.NewInt32(table, "AVAILABLE_FLAG")
	e.CreatedAt = field.NewTime(table, "CREATED_TIME")
	e.UpdatedAt = field.NewTime(table, "UPDATED_TIME")

	e.fillFieldMap()

	return e
}

func (e *eleFile) GetFieldByName(fieldName string) (field.OrderExpr, bool) {
	_f, ok := e.fieldMap[fieldName]
	if !ok || _f == nil {
		return nil, false
	}
	_oe, ok := _f.(field.OrderExpr)
	return _oe, ok
}

func (e *eleFile) fillFieldMap() {
	e.fieldMap = make(map[string]field.Expr, 10)
	e.fieldMap["ID"] = e.ID
	e.fieldMap["ELE_ID"] = e.ELEID
	e.fieldMap["NAME"] = e.NAME
	e.fieldMap["TYPE"] = e.TYPE
	e.fieldMap["PATH"] = e.PATH
	e.fieldMap["PAGE_NUM"] = e.PAGENUM
	e.fieldMap["COMPLETED_FLAG"] = e.COMPLETEDFLAG
	e.fieldMap["AVAILABLE_FLAG"] = e.AVAILABLEFLAG
	e.fieldMap["CREATED_TIME"] = e.CreatedAt
	e.fieldMap["UPDATED_TIME"] = e.UpdatedAt
}

func (e eleFile) clone(db *gorm.DB) eleFile {
	e.eleFileDo.ReplaceDB(db)
	return e
}

type eleFileDo struct{ gen.DO }

func (e eleFileDo) Debug() *eleFileDo {
	return e.withDO(e.DO.Debug())
}

func (e eleFileDo) WithContext(ctx context.Context) *eleFileDo {
	return e.withDO(e.DO.WithContext(ctx))
}

func (e eleFileDo) ReadDB() *eleFileDo {
	return e.Clauses(dbresolver.Read)
}

func (e eleFileDo) WriteDB() *eleFileDo {
	return e.Clauses(dbresolver.Write)
}

func (e eleFileDo) Clauses(conds ...clause.Expression) *eleFileDo {
	return e.withDO(e.DO.Clauses(conds...))
}

func (e eleFileDo) Returning(value interface{}, columns ...string) *eleFileDo {
	return e.withDO(e.DO.Returning(value, columns...))
}

func (e eleFileDo) Not(conds ...gen.Condition) *eleFileDo {
	return e.withDO(e.DO.Not(conds...))
}

func (e eleFileDo) Or(conds ...gen.Condition) *eleFileDo {
	return e.withDO(e.DO.Or(conds...))
}

func (e eleFileDo) Select(conds ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.Select(conds...))
}

func (e eleFileDo) Where(conds ...gen.Condition) *eleFileDo {
	return e.withDO(e.DO.Where(conds...))
}

func (e eleFileDo) Exists(subquery interface{ UnderlyingDB() *gorm.DB }) *eleFileDo {
	return e.Where(field.CompareSubQuery(field.ExistsOp, nil, subquery.UnderlyingDB()))
}

func (e eleFileDo) Order(conds ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.Order(conds...))
}

func (e eleFileDo) Distinct(cols ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.Distinct(cols...))
}

func (e eleFileDo) Omit(cols ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.Omit(cols...))
}

func (e eleFileDo) Join(table schema.Tabler, on ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.Join(table, on...))
}

func (e eleFileDo) LeftJoin(table schema.Tabler, on ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.LeftJoin(table, on...))
}

func (e eleFileDo) RightJoin(table schema.Tabler, on ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.RightJoin(table, on...))
}

func (e eleFileDo) Group(cols ...field.Expr) *eleFileDo {
	return e.withDO(e.DO.Group(cols...))
}

func (e eleFileDo) Having(conds ...gen.Condition) *eleFileDo {
	return e.withDO(e.DO.Having(conds...))
}

func (e eleFileDo) Limit(limit int) *eleFileDo {
	return e.withDO(e.DO.Limit(limit))
}

func (e eleFileDo) Offset(offset int) *eleFileDo {
	return e.withDO(e.DO.Offset(offset))
}

func (e eleFileDo) Scopes(funcs ...func(gen.Dao) gen.Dao) *eleFileDo {
	return e.withDO(e.DO.Scopes(funcs...))
}

func (e eleFileDo) Unscoped() *eleFileDo {
	return e.withDO(e.DO.Unscoped())
}

func (e eleFileDo) Create(values ...*dao.EleFile) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Create(values)
}

func (e eleFileDo) CreateInBatches(values []*dao.EleFile, batchSize int) error {
	return e.DO.CreateInBatches(values, batchSize)
}

// Save : !!! underlying implementation is different with GORM
// The method is equivalent to executing the statement: db.Clauses(clause.OnConflict{UpdateAll: true}).Create(values)
func (e eleFileDo) Save(values ...*dao.EleFile) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Save(values)
}

func (e eleFileDo) First() (*dao.EleFile, error) {
	if result, err := e.DO.First(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleFile), nil
	}
}

func (e eleFileDo) Take() (*dao.EleFile, error) {
	if result, err := e.DO.Take(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleFile), nil
	}
}

func (e eleFileDo) Last() (*dao.EleFile, error) {
	if result, err := e.DO.Last(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleFile), nil
	}
}

func (e eleFileDo) Find() ([]*dao.EleFile, error) {
	result, err := e.DO.Find()
	return result.([]*dao.EleFile), err
}

func (e eleFileDo) FindInBatch(batchSize int, fc func(tx gen.Dao, batch int) error) (results []*dao.EleFile, err error) {
	buf := make([]*dao.EleFile, 0, batchSize)
	err = e.DO.FindInBatches(&buf, batchSize, func(tx gen.Dao, batch int) error {
		defer func() { results = append(results, buf...) }()
		return fc(tx, batch)
	})
	return results, err
}

func (e eleFileDo) FindInBatches(result *[]*dao.EleFile, batchSize int, fc func(tx gen.Dao, batch int) error) error {
	return e.DO.FindInBatches(result, batchSize, fc)
}

func (e eleFileDo) Attrs(attrs ...field.AssignExpr) *eleFileDo {
	return e.withDO(e.DO.Attrs(attrs...))
}

func (e eleFileDo) Assign(attrs ...field.AssignExpr) *eleFileDo {
	return e.withDO(e.DO.Assign(attrs...))
}

func (e eleFileDo) Joins(fields ...field.RelationField) *eleFileDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Joins(_f))
	}
	return &e
}

func (e eleFileDo) Preload(fields ...field.RelationField) *eleFileDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Preload(_f))
	}
	return &e
}

func (e eleFileDo) FirstOrInit() (*dao.EleFile, error) {
	if result, err := e.DO.FirstOrInit(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleFile), nil
	}
}

func (e eleFileDo) FirstOrCreate() (*dao.EleFile, error) {
	if result, err := e.DO.FirstOrCreate(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleFile), nil
	}
}

func (e eleFileDo) FindByPage(offset int, limit int) (result []*dao.EleFile, count int64, err error) {
	result, err = e.Offset(offset).Limit(limit).Find()
	if err != nil {
		return
	}

	if size := len(result); 0 < limit && 0 < size && size < limit {
		count = int64(size + offset)
		return
	}

	count, err = e.Offset(-1).Limit(-1).Count()
	return
}

func (e eleFileDo) ScanByPage(result interface{}, offset int, limit int) (count int64, err error) {
	count, err = e.Count()
	if err != nil {
		return
	}

	err = e.Offset(offset).Limit(limit).Scan(result)
	return
}

func (e eleFileDo) Scan(result interface{}) (err error) {
	return e.DO.Scan(result)
}

func (e eleFileDo) Delete(models ...*dao.EleFile) (result gen.ResultInfo, err error) {
	return e.DO.Delete(models)
}

func (e *eleFileDo) withDO(do gen.Dao) *eleFileDo {
	e.DO = *do.(*gen.DO)
	return e
}
