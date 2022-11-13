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

	"pelican/internal/dal/dao"
)

func newEleActorRe(db *gorm.DB) eleActorRe {
	_eleActorRe := eleActorRe{}

	_eleActorRe.eleActorReDo.UseDB(db)
	_eleActorRe.eleActorReDo.UseModel(&dao.EleActorRe{})

	tableName := _eleActorRe.eleActorReDo.TableName()
	_eleActorRe.ALL = field.NewAsterisk(tableName)
	_eleActorRe.ID = field.NewInt32(tableName, "ID")
	_eleActorRe.ELEID = field.NewInt64(tableName, "ELE_ID")
	_eleActorRe.ACTORID = field.NewInt32(tableName, "ACTOR_ID")
	_eleActorRe.CreatedAt = field.NewTime(tableName, "CREATED_TIME")
	_eleActorRe.UpdatedAt = field.NewTime(tableName, "UPDATED_TIME")

	_eleActorRe.fillFieldMap()

	return _eleActorRe
}

type eleActorRe struct {
	eleActorReDo

	ALL       field.Asterisk
	ID        field.Int32
	ELEID     field.Int64
	ACTORID   field.Int32
	CreatedAt field.Time
	UpdatedAt field.Time

	fieldMap map[string]field.Expr
}

func (e eleActorRe) Table(newTableName string) *eleActorRe {
	e.eleActorReDo.UseTable(newTableName)
	return e.updateTableName(newTableName)
}

func (e eleActorRe) As(alias string) *eleActorRe {
	e.eleActorReDo.DO = *(e.eleActorReDo.As(alias).(*gen.DO))
	return e.updateTableName(alias)
}

func (e *eleActorRe) updateTableName(table string) *eleActorRe {
	e.ALL = field.NewAsterisk(table)
	e.ID = field.NewInt32(table, "ID")
	e.ELEID = field.NewInt64(table, "ELE_ID")
	e.ACTORID = field.NewInt32(table, "ACTOR_ID")
	e.CreatedAt = field.NewTime(table, "CREATED_TIME")
	e.UpdatedAt = field.NewTime(table, "UPDATED_TIME")

	e.fillFieldMap()

	return e
}

func (e *eleActorRe) GetFieldByName(fieldName string) (field.OrderExpr, bool) {
	_f, ok := e.fieldMap[fieldName]
	if !ok || _f == nil {
		return nil, false
	}
	_oe, ok := _f.(field.OrderExpr)
	return _oe, ok
}

func (e *eleActorRe) fillFieldMap() {
	e.fieldMap = make(map[string]field.Expr, 5)
	e.fieldMap["ID"] = e.ID
	e.fieldMap["ELE_ID"] = e.ELEID
	e.fieldMap["ACTOR_ID"] = e.ACTORID
	e.fieldMap["CREATED_TIME"] = e.CreatedAt
	e.fieldMap["UPDATED_TIME"] = e.UpdatedAt
}

func (e eleActorRe) clone(db *gorm.DB) eleActorRe {
	e.eleActorReDo.ReplaceDB(db)
	return e
}

type eleActorReDo struct{ gen.DO }

func (e eleActorReDo) Debug() *eleActorReDo {
	return e.withDO(e.DO.Debug())
}

func (e eleActorReDo) WithContext(ctx context.Context) *eleActorReDo {
	return e.withDO(e.DO.WithContext(ctx))
}

func (e eleActorReDo) ReadDB() *eleActorReDo {
	return e.Clauses(dbresolver.Read)
}

func (e eleActorReDo) WriteDB() *eleActorReDo {
	return e.Clauses(dbresolver.Write)
}

func (e eleActorReDo) Clauses(conds ...clause.Expression) *eleActorReDo {
	return e.withDO(e.DO.Clauses(conds...))
}

func (e eleActorReDo) Returning(value interface{}, columns ...string) *eleActorReDo {
	return e.withDO(e.DO.Returning(value, columns...))
}

func (e eleActorReDo) Not(conds ...gen.Condition) *eleActorReDo {
	return e.withDO(e.DO.Not(conds...))
}

func (e eleActorReDo) Or(conds ...gen.Condition) *eleActorReDo {
	return e.withDO(e.DO.Or(conds...))
}

func (e eleActorReDo) Select(conds ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.Select(conds...))
}

func (e eleActorReDo) Where(conds ...gen.Condition) *eleActorReDo {
	return e.withDO(e.DO.Where(conds...))
}

func (e eleActorReDo) Exists(subquery interface{ UnderlyingDB() *gorm.DB }) *eleActorReDo {
	return e.Where(field.CompareSubQuery(field.ExistsOp, nil, subquery.UnderlyingDB()))
}

func (e eleActorReDo) Order(conds ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.Order(conds...))
}

func (e eleActorReDo) Distinct(cols ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.Distinct(cols...))
}

func (e eleActorReDo) Omit(cols ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.Omit(cols...))
}

func (e eleActorReDo) Join(table schema.Tabler, on ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.Join(table, on...))
}

func (e eleActorReDo) LeftJoin(table schema.Tabler, on ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.LeftJoin(table, on...))
}

func (e eleActorReDo) RightJoin(table schema.Tabler, on ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.RightJoin(table, on...))
}

func (e eleActorReDo) Group(cols ...field.Expr) *eleActorReDo {
	return e.withDO(e.DO.Group(cols...))
}

func (e eleActorReDo) Having(conds ...gen.Condition) *eleActorReDo {
	return e.withDO(e.DO.Having(conds...))
}

func (e eleActorReDo) Limit(limit int) *eleActorReDo {
	return e.withDO(e.DO.Limit(limit))
}

func (e eleActorReDo) Offset(offset int) *eleActorReDo {
	return e.withDO(e.DO.Offset(offset))
}

func (e eleActorReDo) Scopes(funcs ...func(gen.Dao) gen.Dao) *eleActorReDo {
	return e.withDO(e.DO.Scopes(funcs...))
}

func (e eleActorReDo) Unscoped() *eleActorReDo {
	return e.withDO(e.DO.Unscoped())
}

func (e eleActorReDo) Create(values ...*dao.EleActorRe) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Create(values)
}

func (e eleActorReDo) CreateInBatches(values []*dao.EleActorRe, batchSize int) error {
	return e.DO.CreateInBatches(values, batchSize)
}

// Save : !!! underlying implementation is different with GORM
// The method is equivalent to executing the statement: db.Clauses(clause.OnConflict{UpdateAll: true}).Create(values)
func (e eleActorReDo) Save(values ...*dao.EleActorRe) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Save(values)
}

func (e eleActorReDo) First() (*dao.EleActorRe, error) {
	if result, err := e.DO.First(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleActorRe), nil
	}
}

func (e eleActorReDo) Take() (*dao.EleActorRe, error) {
	if result, err := e.DO.Take(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleActorRe), nil
	}
}

func (e eleActorReDo) Last() (*dao.EleActorRe, error) {
	if result, err := e.DO.Last(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleActorRe), nil
	}
}

func (e eleActorReDo) Find() ([]*dao.EleActorRe, error) {
	result, err := e.DO.Find()
	return result.([]*dao.EleActorRe), err
}

func (e eleActorReDo) FindInBatch(batchSize int, fc func(tx gen.Dao, batch int) error) (results []*dao.EleActorRe, err error) {
	buf := make([]*dao.EleActorRe, 0, batchSize)
	err = e.DO.FindInBatches(&buf, batchSize, func(tx gen.Dao, batch int) error {
		defer func() { results = append(results, buf...) }()
		return fc(tx, batch)
	})
	return results, err
}

func (e eleActorReDo) FindInBatches(result *[]*dao.EleActorRe, batchSize int, fc func(tx gen.Dao, batch int) error) error {
	return e.DO.FindInBatches(result, batchSize, fc)
}

func (e eleActorReDo) Attrs(attrs ...field.AssignExpr) *eleActorReDo {
	return e.withDO(e.DO.Attrs(attrs...))
}

func (e eleActorReDo) Assign(attrs ...field.AssignExpr) *eleActorReDo {
	return e.withDO(e.DO.Assign(attrs...))
}

func (e eleActorReDo) Joins(fields ...field.RelationField) *eleActorReDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Joins(_f))
	}
	return &e
}

func (e eleActorReDo) Preload(fields ...field.RelationField) *eleActorReDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Preload(_f))
	}
	return &e
}

func (e eleActorReDo) FirstOrInit() (*dao.EleActorRe, error) {
	if result, err := e.DO.FirstOrInit(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleActorRe), nil
	}
}

func (e eleActorReDo) FirstOrCreate() (*dao.EleActorRe, error) {
	if result, err := e.DO.FirstOrCreate(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EleActorRe), nil
	}
}

func (e eleActorReDo) FindByPage(offset int, limit int) (result []*dao.EleActorRe, count int64, err error) {
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

func (e eleActorReDo) ScanByPage(result interface{}, offset int, limit int) (count int64, err error) {
	count, err = e.Count()
	if err != nil {
		return
	}

	err = e.Offset(offset).Limit(limit).Scan(result)
	return
}

func (e eleActorReDo) Scan(result interface{}) (err error) {
	return e.DO.Scan(result)
}

func (e eleActorReDo) Delete(models ...*dao.EleActorRe) (result gen.ResultInfo, err error) {
	return e.DO.Delete(models)
}

func (e *eleActorReDo) withDO(do gen.Dao) *eleActorReDo {
	e.DO = *do.(*gen.DO)
	return e
}
