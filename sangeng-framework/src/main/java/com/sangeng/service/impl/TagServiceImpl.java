package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagVo;
import com.sangeng.mapper.TagMapper;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-10-12 21:15:36
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Strings.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(Strings.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> tagPage = new Page<>(pageNum,pageSize);
        page(tagPage,queryWrapper);
        List<Tag> records = tagPage.getRecords();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(records, TagVo.class);
        PageVo pageVo = new PageVo(tagVos, tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getAllTags() {
        List<Tag> list = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        tagVos.stream()
                .map(t->t.setRemark(null))
                .collect(Collectors.toList());
        return ResponseResult.okResult(tagVos);
    }
}
