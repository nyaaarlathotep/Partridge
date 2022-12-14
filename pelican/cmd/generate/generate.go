package main

import (
	"gorm.io/driver/mysql"
	"gorm.io/gen"
	"gorm.io/gen/field"
	"gorm.io/gorm"
	"javCrawl/internal/dal/dao"
)

// generate code
func main() {
	// specify the output directory (default: "./query")
	// ### if you want to query without context constrain, set mode gen.WithoutContext ###
	const dalBase = "./internal/dal"
	g := gen.NewGenerator(gen.Config{
		OutPath:       dalBase + "/query",
		OutFile:       dalBase + "/query/gen.go",
		ModelPkgPath:  dalBase + "/dao",
		FieldNullable: false,
		Mode:          gen.WithoutContext | gen.WithDefaultQuery,
		//if you want the nullable field generation property to be pointer type, set FieldNullable true
		/* FieldNullable: true,*/
		//if you want to assign field which has default value in `Create` API, set FieldCoverable true, reference: https://gorm.io/docs/create.html#Default-Values
		/* FieldCoverable: true,*/
		// if you want generate field with unsigned integer type, set FieldSignable true
		/* FieldSignable: true,*/
		//if you want to generate index tags from database, set FieldWithIndexTag true
		FieldWithIndexTag: true,
		//if you want to generate type tags from database, set FieldWithTypeTag true
		FieldWithTypeTag: true,
		//if you need unit tests for query code, set WithUnitTest true
		WithUnitTest: false,
	})

	// reuse the database connection in Project or create a connection here
	// if you want to use GenerateModel/GenerateModelAs, UseDB is necessary or it will panic
	db, _ := gorm.Open(mysql.Open("root:12345678@tcp(127.0.0.1:3306)/partridge?charset=utf8mb4&parseTime=True&loc=Local"))
	//db, _ := gorm.Open(mysql.Open("root:@(127.0.0.1:3306)/demo?charset=utf8mb4&parseTime=True&loc=Local"))
	g.UseDB(db)

	// apply basic crud api on structs or table models which is specified by table name with function
	// GenerateModel/GenerateModelAs. And generator will generate table models' code when calling Excute.
	// 想对已有的model生成crud等基础方法可以直接指定model struct ，例如model.User{}
	// 如果是想直接生成表的model和crud方法，则可以指定表的名称，例如g.GenerateModel("company")
	// 想自定义某个表生成特性，比如struct的名称/字段类型/tag等，可以指定opt，例如g.GenerateModel("company",gen.FieldIgnore("address")), g.GenerateModelAs("people", "Person", gen.FieldIgnore("address"))
	g.ApplyBasic(dao.EleActorRe{}, dao.EleAuthorRe{}, dao.EleOrgRe{}, dao.EleTagRe{})

	g.ApplyBasic(g.GenerateModelAs("element", "Element",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt"),
		gen.FieldRelateModel(field.HasMany, "EleFile", dao.EleFile{}, &field.RelateConfig{
			GORMTag: "foreignKey:ELE_ID",
		}),
		gen.FieldRelateModel(field.Many2Many, "Actor", dao.Actor{}, &field.RelateConfig{
			GORMTag: "many2many:ele_actor_re;" +
				"foreignKey:ID;joinForeignKey:ELE_ID;References:ID;joinReferences:ACTOR_ID",
		}),
		gen.FieldRelateModel(field.Many2Many, "Author", dao.Author{}, &field.RelateConfig{
			GORMTag: "many2many:ele_author_re;" +
				"foreignKey:ID;joinForeignKey:ELE_ID;References:ID;joinReferences:AUTHOR_ID",
		}),
		gen.FieldRelateModel(field.Many2Many, "Organization", dao.Organization{}, &field.RelateConfig{
			GORMTag: "many2many:ele_org_re;" +
				"foreignKey:ID;joinForeignKey:ELE_ID;References:ID;joinReferences:ORG_ID",
		}),
		gen.FieldRelateModel(field.Many2Many, "TagInfo", dao.TagInfo{}, &field.RelateConfig{
			GORMTag: "many2many:ele_tag_re;" +
				"foreignKey:ID;joinForeignKey:ELE_ID;References:ID;joinReferences:TAG_ID",
		}),
	))
	g.ApplyBasic(g.GenerateModelAs("actor", "Actor",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("jav", "Jav",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("ehentai_gallery", "EhentaiGallery",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("ele_file", "EleFile",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("organization", "Organization",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("author", "Author",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("tag_info", "TagInfo",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	g.ApplyBasic(g.GenerateModelAs("pr_user", "PrUser",
		gen.FieldRename("CREATED_TIME", "CreatedAt"),
		gen.FieldRename("UPDATED_TIME", "UpdatedAt")))
	//g.ApplyBasic(g.GenerateAllTable())
	// apply diy interfaces on structs or table models
	// 如果想给某些表或者model生成自定义方法，可以用ApplyInterface，第一个参数是方法接口，可以参考DIY部分文档定义
	//g.ApplyInterface(func(method model.Method) {}, model.User{}, g.GenerateModel("company"))

	// execute the action of code generation
	g.Execute()
}
