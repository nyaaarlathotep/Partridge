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

func newEhentaiGallery(db *gorm.DB) ehentaiGallery {
	_ehentaiGallery := ehentaiGallery{}

	_ehentaiGallery.ehentaiGalleryDo.UseDB(db)
	_ehentaiGallery.ehentaiGalleryDo.UseModel(&dao.EhentaiGallery{})

	tableName := _ehentaiGallery.ehentaiGalleryDo.TableName()
	_ehentaiGallery.ALL = field.NewAsterisk(tableName)
	_ehentaiGallery.GID = field.NewInt64(tableName, "GID")
	_ehentaiGallery.ELEID = field.NewInt64(tableName, "ELE_ID")
	_ehentaiGallery.TITLE = field.NewString(tableName, "TITLE")
	_ehentaiGallery.TITLEJPN = field.NewString(tableName, "TITLE_JPN")
	_ehentaiGallery.CATEGORY = field.NewInt32(tableName, "CATEGORY")
	_ehentaiGallery.UPLOADER = field.NewString(tableName, "UPLOADER")
	_ehentaiGallery.RATING = field.NewFloat64(tableName, "RATING")
	_ehentaiGallery.RATINGCOUNT = field.NewInt32(tableName, "RATING_COUNT")
	_ehentaiGallery.PAGES = field.NewInt32(tableName, "PAGES")
	_ehentaiGallery.PREVIEWPAGE = field.NewInt32(tableName, "PREVIEW_PAGE")
	_ehentaiGallery.TOKEN = field.NewString(tableName, "TOKEN")
	_ehentaiGallery.POSTED = field.NewTime(tableName, "POSTED")
	_ehentaiGallery.FAVORITECOUNT = field.NewInt32(tableName, "FAVORITE_COUNT")
	_ehentaiGallery.CASHEDFLAG = field.NewInt32(tableName, "CASHED_FLAG")
	_ehentaiGallery.DOWNLOADFLAG = field.NewInt32(tableName, "DOWNLOAD_FLAG")
	_ehentaiGallery.CreatedAt = field.NewTime(tableName, "CREATED_TIME")
	_ehentaiGallery.UpdatedAt = field.NewTime(tableName, "UPDATED_TIME")

	_ehentaiGallery.fillFieldMap()

	return _ehentaiGallery
}

type ehentaiGallery struct {
	ehentaiGalleryDo

	ALL           field.Asterisk
	GID           field.Int64 // ehentai gallery id
	ELEID         field.Int64
	TITLE         field.String
	TITLEJPN      field.String
	CATEGORY      field.Int32   // gallery分类
	UPLOADER      field.String  // 上传者
	RATING        field.Float64 // 评分
	RATINGCOUNT   field.Int32   // 评分人数
	PAGES         field.Int32   // 总页数
	PREVIEWPAGE   field.Int32   // 预览画廊对应页
	TOKEN         field.String  // gtoken
	POSTED        field.Time    // 上传时间
	FAVORITECOUNT field.Int32   // 喜爱数
	CASHEDFLAG    field.Int32   // (0-否;1-是)
	DOWNLOADFLAG  field.Int32   // (0-否;1-是)
	CreatedAt     field.Time
	UpdatedAt     field.Time

	fieldMap map[string]field.Expr
}

func (e ehentaiGallery) Table(newTableName string) *ehentaiGallery {
	e.ehentaiGalleryDo.UseTable(newTableName)
	return e.updateTableName(newTableName)
}

func (e ehentaiGallery) As(alias string) *ehentaiGallery {
	e.ehentaiGalleryDo.DO = *(e.ehentaiGalleryDo.As(alias).(*gen.DO))
	return e.updateTableName(alias)
}

func (e *ehentaiGallery) updateTableName(table string) *ehentaiGallery {
	e.ALL = field.NewAsterisk(table)
	e.GID = field.NewInt64(table, "GID")
	e.ELEID = field.NewInt64(table, "ELE_ID")
	e.TITLE = field.NewString(table, "TITLE")
	e.TITLEJPN = field.NewString(table, "TITLE_JPN")
	e.CATEGORY = field.NewInt32(table, "CATEGORY")
	e.UPLOADER = field.NewString(table, "UPLOADER")
	e.RATING = field.NewFloat64(table, "RATING")
	e.RATINGCOUNT = field.NewInt32(table, "RATING_COUNT")
	e.PAGES = field.NewInt32(table, "PAGES")
	e.PREVIEWPAGE = field.NewInt32(table, "PREVIEW_PAGE")
	e.TOKEN = field.NewString(table, "TOKEN")
	e.POSTED = field.NewTime(table, "POSTED")
	e.FAVORITECOUNT = field.NewInt32(table, "FAVORITE_COUNT")
	e.CASHEDFLAG = field.NewInt32(table, "CASHED_FLAG")
	e.DOWNLOADFLAG = field.NewInt32(table, "DOWNLOAD_FLAG")
	e.CreatedAt = field.NewTime(table, "CREATED_TIME")
	e.UpdatedAt = field.NewTime(table, "UPDATED_TIME")

	e.fillFieldMap()

	return e
}

func (e *ehentaiGallery) GetFieldByName(fieldName string) (field.OrderExpr, bool) {
	_f, ok := e.fieldMap[fieldName]
	if !ok || _f == nil {
		return nil, false
	}
	_oe, ok := _f.(field.OrderExpr)
	return _oe, ok
}

func (e *ehentaiGallery) fillFieldMap() {
	e.fieldMap = make(map[string]field.Expr, 17)
	e.fieldMap["GID"] = e.GID
	e.fieldMap["ELE_ID"] = e.ELEID
	e.fieldMap["TITLE"] = e.TITLE
	e.fieldMap["TITLE_JPN"] = e.TITLEJPN
	e.fieldMap["CATEGORY"] = e.CATEGORY
	e.fieldMap["UPLOADER"] = e.UPLOADER
	e.fieldMap["RATING"] = e.RATING
	e.fieldMap["RATING_COUNT"] = e.RATINGCOUNT
	e.fieldMap["PAGES"] = e.PAGES
	e.fieldMap["PREVIEW_PAGE"] = e.PREVIEWPAGE
	e.fieldMap["TOKEN"] = e.TOKEN
	e.fieldMap["POSTED"] = e.POSTED
	e.fieldMap["FAVORITE_COUNT"] = e.FAVORITECOUNT
	e.fieldMap["CASHED_FLAG"] = e.CASHEDFLAG
	e.fieldMap["DOWNLOAD_FLAG"] = e.DOWNLOADFLAG
	e.fieldMap["CREATED_TIME"] = e.CreatedAt
	e.fieldMap["UPDATED_TIME"] = e.UpdatedAt
}

func (e ehentaiGallery) clone(db *gorm.DB) ehentaiGallery {
	e.ehentaiGalleryDo.ReplaceDB(db)
	return e
}

type ehentaiGalleryDo struct{ gen.DO }

func (e ehentaiGalleryDo) Debug() *ehentaiGalleryDo {
	return e.withDO(e.DO.Debug())
}

func (e ehentaiGalleryDo) WithContext(ctx context.Context) *ehentaiGalleryDo {
	return e.withDO(e.DO.WithContext(ctx))
}

func (e ehentaiGalleryDo) ReadDB() *ehentaiGalleryDo {
	return e.Clauses(dbresolver.Read)
}

func (e ehentaiGalleryDo) WriteDB() *ehentaiGalleryDo {
	return e.Clauses(dbresolver.Write)
}

func (e ehentaiGalleryDo) Clauses(conds ...clause.Expression) *ehentaiGalleryDo {
	return e.withDO(e.DO.Clauses(conds...))
}

func (e ehentaiGalleryDo) Returning(value interface{}, columns ...string) *ehentaiGalleryDo {
	return e.withDO(e.DO.Returning(value, columns...))
}

func (e ehentaiGalleryDo) Not(conds ...gen.Condition) *ehentaiGalleryDo {
	return e.withDO(e.DO.Not(conds...))
}

func (e ehentaiGalleryDo) Or(conds ...gen.Condition) *ehentaiGalleryDo {
	return e.withDO(e.DO.Or(conds...))
}

func (e ehentaiGalleryDo) Select(conds ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Select(conds...))
}

func (e ehentaiGalleryDo) Where(conds ...gen.Condition) *ehentaiGalleryDo {
	return e.withDO(e.DO.Where(conds...))
}

func (e ehentaiGalleryDo) Exists(subquery interface{ UnderlyingDB() *gorm.DB }) *ehentaiGalleryDo {
	return e.Where(field.CompareSubQuery(field.ExistsOp, nil, subquery.UnderlyingDB()))
}

func (e ehentaiGalleryDo) Order(conds ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Order(conds...))
}

func (e ehentaiGalleryDo) Distinct(cols ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Distinct(cols...))
}

func (e ehentaiGalleryDo) Omit(cols ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Omit(cols...))
}

func (e ehentaiGalleryDo) Join(table schema.Tabler, on ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Join(table, on...))
}

func (e ehentaiGalleryDo) LeftJoin(table schema.Tabler, on ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.LeftJoin(table, on...))
}

func (e ehentaiGalleryDo) RightJoin(table schema.Tabler, on ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.RightJoin(table, on...))
}

func (e ehentaiGalleryDo) Group(cols ...field.Expr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Group(cols...))
}

func (e ehentaiGalleryDo) Having(conds ...gen.Condition) *ehentaiGalleryDo {
	return e.withDO(e.DO.Having(conds...))
}

func (e ehentaiGalleryDo) Limit(limit int) *ehentaiGalleryDo {
	return e.withDO(e.DO.Limit(limit))
}

func (e ehentaiGalleryDo) Offset(offset int) *ehentaiGalleryDo {
	return e.withDO(e.DO.Offset(offset))
}

func (e ehentaiGalleryDo) Scopes(funcs ...func(gen.Dao) gen.Dao) *ehentaiGalleryDo {
	return e.withDO(e.DO.Scopes(funcs...))
}

func (e ehentaiGalleryDo) Unscoped() *ehentaiGalleryDo {
	return e.withDO(e.DO.Unscoped())
}

func (e ehentaiGalleryDo) Create(values ...*dao.EhentaiGallery) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Create(values)
}

func (e ehentaiGalleryDo) CreateInBatches(values []*dao.EhentaiGallery, batchSize int) error {
	return e.DO.CreateInBatches(values, batchSize)
}

// Save : !!! underlying implementation is different with GORM
// The method is equivalent to executing the statement: db.Clauses(clause.OnConflict{UpdateAll: true}).Create(values)
func (e ehentaiGalleryDo) Save(values ...*dao.EhentaiGallery) error {
	if len(values) == 0 {
		return nil
	}
	return e.DO.Save(values)
}

func (e ehentaiGalleryDo) First() (*dao.EhentaiGallery, error) {
	if result, err := e.DO.First(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EhentaiGallery), nil
	}
}

func (e ehentaiGalleryDo) Take() (*dao.EhentaiGallery, error) {
	if result, err := e.DO.Take(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EhentaiGallery), nil
	}
}

func (e ehentaiGalleryDo) Last() (*dao.EhentaiGallery, error) {
	if result, err := e.DO.Last(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EhentaiGallery), nil
	}
}

func (e ehentaiGalleryDo) Find() ([]*dao.EhentaiGallery, error) {
	result, err := e.DO.Find()
	return result.([]*dao.EhentaiGallery), err
}

func (e ehentaiGalleryDo) FindInBatch(batchSize int, fc func(tx gen.Dao, batch int) error) (results []*dao.EhentaiGallery, err error) {
	buf := make([]*dao.EhentaiGallery, 0, batchSize)
	err = e.DO.FindInBatches(&buf, batchSize, func(tx gen.Dao, batch int) error {
		defer func() { results = append(results, buf...) }()
		return fc(tx, batch)
	})
	return results, err
}

func (e ehentaiGalleryDo) FindInBatches(result *[]*dao.EhentaiGallery, batchSize int, fc func(tx gen.Dao, batch int) error) error {
	return e.DO.FindInBatches(result, batchSize, fc)
}

func (e ehentaiGalleryDo) Attrs(attrs ...field.AssignExpr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Attrs(attrs...))
}

func (e ehentaiGalleryDo) Assign(attrs ...field.AssignExpr) *ehentaiGalleryDo {
	return e.withDO(e.DO.Assign(attrs...))
}

func (e ehentaiGalleryDo) Joins(fields ...field.RelationField) *ehentaiGalleryDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Joins(_f))
	}
	return &e
}

func (e ehentaiGalleryDo) Preload(fields ...field.RelationField) *ehentaiGalleryDo {
	for _, _f := range fields {
		e = *e.withDO(e.DO.Preload(_f))
	}
	return &e
}

func (e ehentaiGalleryDo) FirstOrInit() (*dao.EhentaiGallery, error) {
	if result, err := e.DO.FirstOrInit(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EhentaiGallery), nil
	}
}

func (e ehentaiGalleryDo) FirstOrCreate() (*dao.EhentaiGallery, error) {
	if result, err := e.DO.FirstOrCreate(); err != nil {
		return nil, err
	} else {
		return result.(*dao.EhentaiGallery), nil
	}
}

func (e ehentaiGalleryDo) FindByPage(offset int, limit int) (result []*dao.EhentaiGallery, count int64, err error) {
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

func (e ehentaiGalleryDo) ScanByPage(result interface{}, offset int, limit int) (count int64, err error) {
	count, err = e.Count()
	if err != nil {
		return
	}

	err = e.Offset(offset).Limit(limit).Scan(result)
	return
}

func (e ehentaiGalleryDo) Scan(result interface{}) (err error) {
	return e.DO.Scan(result)
}

func (e ehentaiGalleryDo) Delete(models ...*dao.EhentaiGallery) (result gen.ResultInfo, err error) {
	return e.DO.Delete(models)
}

func (e *ehentaiGalleryDo) withDO(do gen.Dao) *ehentaiGalleryDo {
	e.DO = *do.(*gen.DO)
	return e
}