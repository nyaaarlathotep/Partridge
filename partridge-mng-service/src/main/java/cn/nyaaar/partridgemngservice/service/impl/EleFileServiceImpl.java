package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.mapper.EleFileMapper;
import cn.nyaaar.partridgemngservice.model.eh.GalleryPage;
import cn.nyaaar.partridgemngservice.service.EleFileService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-26
 */
@Service
public class EleFileServiceImpl extends ServiceImpl<EleFileMapper, EleFile> implements EleFileService {

    @Override
    public QueryData<EleFile> findListByPage(EleFile where, Integer page, Integer pageCount){
        IPage<EleFile> wherePage = new Page<>(page, pageCount);

        IPage<EleFile> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EleFile> findList(EleFile where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EleFile eleFile){
 
        return baseMapper.insert(eleFile);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EleFile eleFile){
    
        return baseMapper.updateById(eleFile);
    }

    @Override
    public EleFile findById(Integer id){
    
        return baseMapper.selectById(id);
    }

    @Override
    public GalleryPage getGalleryPage(EleFile eleFile) {
        File file = new File(eleFile.getPath());
        String fileBase64 = "";
        if (file.exists()) {
            fileBase64 = FileUtil.file2Base64(file);
        }
        return new GalleryPage()
                .setPageIndex(eleFile.getPageNum())
                .setPageBase64(fileBase64)
                .setFileSuffix(Objects.requireNonNullElse(
                        FileTypeEnum.getTypeBySuffix(eleFile.getName()),
                        FileTypeEnum.unknown
                ).getSuffix());
    }

    @Override
    public void saveBytesToFile(byte[] bytes, String destDic, String fileName, boolean reDownload) throws IOException {

        Path dic = Path.of(destDic);
        if (Files.notExists(dic)) {
            Files.createDirectories(dic);
        }
        Path filePath = Path.of(PathUtil.simpleConcatUrl(destDic, fileName));
        if (reDownload || Files.notExists(filePath)) {
            Files.write(filePath, bytes, StandardOpenOption.CREATE);
        }
    }
}
