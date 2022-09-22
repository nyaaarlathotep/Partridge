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

func newEleOrgRe(db *gorm.DB) eleOrgRe {
	_eleOrgRe := eleOrgRe{}

	_eleOrgRe.eleOrgReDo.UseDB(db)
	_eleOrgRe.eleOrgReDo.UseModel(&dao.EleOrgRe{})

	tableName := _eleOrgRe.eleOrgReDo.TableName()
	_eleOrgRe.ALL = field.NewAsterisk(tableName)
	_eleOrgRe.ID = field.NewInt32(tableName, "ID")
	_eleOrgRe.ELEID = field.NewInt64(tableName, "ELE_ID")
	_eleOrgRe.ORGID = field.NewInt32(tableName, "ORG_ID")
	_eleOrgRe.CreatedAt = field.NewTime(tableName, "CREATED_TIME")
	_eleOrgRe.UpdatedAt = field.NewTime(tableName, "UPDATED_TIME")

	_eleOrgRe.fillFieldMap()

	return _eleOrgRe
}

type eleOrgRe struct {
	eleOrgReDo

	ALL       field.Asterisk
	ID        field.Int32
	ELEID     field.Int64
	ORGID     field.Int32
	CreatedAt field.Time
	UpdatedAt field.Time

	fieldMap map[string]field.Expr
}

func (e eleOrgRe) Table(newTableName string) *eleOrgRe {
	e.eleOrgReDo.UseTable(newTableName)
	return e.updateTableName(newTableName)
}

func (e eleOrgRe) As(alias string) *eleOrgRe {
	e.eleOrgReDo.DO = *(e.eleOrgReDo.As(alias).(*gen.DO))
	return e.updateTableName(alias)
}

func (e *eleOrgRe) updateTableName(table string) *eleOrgRe {
	e.ALL = field.NewAsterisk(table)
	e.ID = field.NewInt32(table, "ID")
	e.ELEID = field.NewInt64(table, "ELE_ID")
	e.ORGID = field.NewInt32(table, "ORG_ID")
	e.CreatedAt = field.NewTime(table, "CREATED_TIME")
	e.UpdatedAt = field.NewTime(table, "UPDATED_TIME")

	e.fillFieldMap()

	return e
}

func (e *eleOrgRe) GetFieldByName(fieldName string) (field.OrderExpr, bool) {
	_f, ok := e.fieldMap[fieldName]
	if !ok || _f == nil {
		return nil, false
	}
	_oe, ok := _f.(field.OrderExpr)
	return _oe, ok
}

func (e *eleOrgRe) fillFieldMap() {
	e.fieldMap = make(map[string]field.Expr, 5)
	e.fieldMap["ID"] = e.ID
	e.fieldMap["ELE_ID"] = e.ELEID
	e.fieldMap["ORG_ID"] = e.ORGID
	e.fieldMap["CREATED_TIME"] = e.CreatedAt
	e.fieldMap["UPDATED_TIME"] = e.UpdatedAt
}

func (e eleOrgRe) clone(db *gorm.DB) eleOrgRe {
	e.eleOrgReDo.ReplaceDB(db)
	return e
}

type eleOrgReDo struct{ gen.DO }

func (e eleOrgReDo) Debug() *eleOrgReDo {
	return e.withDO(e.DO.Debug())
}

func (e eleOrgReDo) WithContext(ctx context.Context) *eleOrgReDo {
	return e.withDO(e.DO.WithContext(ctx))
}

func (e eleOrgReDo) ReadDB() *eleOrgReDo {
	return e.Clauses(dbresolver.Read)
}

func (e eleOrgReDo) WriteDB() *eleOrgReDo {
	return e.Clauses(dbresolver.Write)
}

func (e eleOrgReDo) Clauses(conds ...clause.Expression) *eleOrgReDo {
	return e.withDO(e.DO.Clauses(conds...))
}

func (e eleOrgReDo) Returning(value interface{}, columns ...string) *eleOrgReDo {
	return e.withDO(e.DO.Returning(value, columns...))
}

func (e eleOrgReDo) Not(conds ...gen.Condition) *eleOrgReDo {
	return e.withDO(e.DO.Not(conds...))
}

func (e eleOrgReDo) Or(conds ...gen.Condition) *eleOrgReDo {
	return e.withDO(e.DO.Or(conds...))
}

func (e eleOrgReDo) Select(conds ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.Select(conds...))
}

func (e eleOrgReDo) Where(conds ...gen.Condition) *eleOrgReDo {
	return e.withDO(e.DO.Where(conds...))
}

func (e eleOrgReDo) Exists(subquery interface{ UnderlyingDB() *gorm.DB }) *eleOrgReDo {
	return e.Where(field.CompareSubQuery(field.ExistsOp, nil, subquery.UnderlyingDB()))
}

func (e eleOrgReDo) Order(conds ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.Order(conds...))
}

func (e eleOrgReDo) Distinct(cols ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.Distinct(cols...))
}

func (e eleOrgReDo) Omit(cols ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.Omit(cols...))
}

func (e eleOrgReDo) Join(table schema.Tabler, on ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.Join(table, on...))
}

func (e eleOrgReDo) LeftJoin(table schema.Tabler, on ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.LeftJoin(table, on...))
}

func (e eleOrgReDo) RightJoin(table schema.Tabler, on ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.RightJoin(table, on...))
}

func (e eleOrgReDo) Group(cols ...field.Expr) *eleOrgReDo {
	return e.withDO(e.DO.Group(cols...))
}

func (e eleOrgReDo) Having(conds ...gen.Condition) *eleOrgReDo {
	return e.withDO(e.DO.Having(conds...))
}

func (e eleOrgReDo) Limit(limit int) *eleOrgReDo {
	return e.withDO(e.DO.Limit(limit))
}

func (e eleOrgReDo) Offset(offset int) *eleOrgReDo {
	return e.withDO(e.DO.Offset(offset))
}

func (e eleOrgReDo) Scopes(funcs ...func(gen.Dao) gen.Dao) *eleOrgReDo {
	return e.withDO(e.DO.Scopes(funcs...))
}

func (e eleOrgReDo) Unscoped() *eleOrgReDo {
	return e.withDO(e.DO.Unscoped())
}

func (e eleOrgReDo) Create(values ...*dao.EleOrgRe) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Create(values)
}

func (e eleOrgReDo) CreateInBatches(values []*dao.EleOrgRe, batchSize int) error {
	return e.DO.CreateInBatches(values, batchSize)
}

// Save : !!! underlying implementation is different with GORM
// The method is equivalent to executing the statement: db.Clauses(clause.OnConflict{UpdateAll: true}).Create(values)
func (e eleOrgReDo) Save(values ...*dao.EleOrgRe) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Save(values)
}

func (e eleOrgReDo) First() (*dao.EleOrgRe, error) {
	if result, err := e.DO.First(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleOrgRe), nil
	}
}

func (e eleOrgReDo) Take() (*dao.EleOrgRe, error) {
	if result, err := e.DO.Take(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleOrgRe), nil
	}
}

func (e eleOrgReDo) Last() (*dao.EleOrgRe, error) {
	if result, err := e.DO.Last(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleOrgRe), nil
	}
}

func (e eleOrgReDo) Find() ([]*dao.EleOrgRe, error) {
	result, err := e.DO.Find()
	return result.([]*dao.EleOrgRe), err
}

func (e eleOrgReDo) FindInBatch(batchSize int, fc func(tx gen.Dao, batch int) error) (results []*dao.EleOrgRe, err error) {
	buf := make([]*dao.EleOrgRe, 0, batchSize)
	err = e.DO.FindInBatches(&buf, batchSize, func(tx gen.Dao, batch int) error {
		defer func() { results = append(results, buf...) }()
		return fc(tx, batch)
	})
	return results, err
}

func (e eleOrgReDo) FindInBatches(result *[]*dao.EleOrgRe, batchSize int, fc func(tx gen.Dao, batch int) error) error {
	return e.DO.FindInBatches(result, batchSize, fc)
}

func (e eleOrgReDo) Attrs(attrs ...field.AssignExpr) *eleOrgReDo {
	return e.withDO(e.DO.Attrs(attrs...))
}

func (e eleOrgReDo) Assign(attrs ...field.AssignExpr) *eleOrgReDo {
	return e.withDO(e.DO.Assign(attrs...))
}

func (e eleOrgReDo) Joins(fields ...field.RelationField) *eleOrgReDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Joins(_f))
	}
	return &e
}

func (e eleOrgReDo) Preload(fields ...field.RelationField) *eleOrgReDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Preload(_f))
	}
	return &e
}

func (e eleOrgReDo) FirstOrInit() (*dao.EleOrgRe, error) {
	if result, err := e.DO.FirstOrInit(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleOrgRe), nil
	}
}

func (e eleOrgReDo) FirstOrCreate() (*dao.EleOrgRe, error) {
	if result, err := e.DO.FirstOrCreate(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleOrgRe), nil
	}
}

func (e eleOrgReDo) FindByPage(offset int, limit int) (result []*dao.EleOrgRe, count int64, err error) {
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

func (e eleOrgReDo) ScanByPage(result interface{}, offset int, limit int) (count int64, err error) {
	count, err = e.Count()
	if err != nil {
		return
	}

	err = e.Offset(offset).Limit(limit).Scan(result)
	return
}

func (e eleOrgReDo) Scan(result interface{}) (err error) {
	return e.DO.Scan(result)
}

func (e eleOrgReDo) Delete(models ...*dao.EleOrgRe) (result gen.ResultInfo, err error) {
	return e.DO.Delete(models)
}

func (e *eleOrgReDo) withDO(do gen.Dao) *eleOrgReDo {
	e.DO = *do.(*gen.DO)
	return e
}
