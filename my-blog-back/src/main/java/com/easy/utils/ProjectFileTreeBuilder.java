package com.easy.utils;

import com.easy.entity.po.ProjectFile;
import com.easy.entity.vo.ProjectFileVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectFileTreeBuilder {

    public static List<ProjectFileVO> build(List<ProjectFile> files) {
        Map<String, ProjectFileVO> pathMap = new HashMap<>();
        List<ProjectFileVO> roots = new ArrayList<>();

        for (ProjectFile file : files) {
            ProjectFileVO vo = toVO(file);
            pathMap.put(file.getPath(), vo);

            String parentPath = getParentPath(file.getPath());

            if (parentPath == null) {
                roots.add(vo);
            } else {
                ProjectFileVO parent = pathMap.get(parentPath);
                if (parent != null) {
                    parent.getChildren().add(vo);
                } else {
                    // 容错：如果父节点还没出现
                    roots.add(vo);
                }
            }
        }

        // 遍历整个树，设置 isDir
        for (ProjectFileVO root : roots) {
            setIsDirRecursively(root);
        }

        return roots;
    }

    // 如果有子节点则认为是目录，否则是文件
    private static void setIsDirRecursively(ProjectFileVO node) {
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            node.setIsDir(true);
            for (ProjectFileVO child : node.getChildren()) {
                setIsDirRecursively(child);
            }
        } else {
            node.setIsDir(false);
        }
    }

    private static ProjectFileVO toVO(ProjectFile file) {
        ProjectFileVO vo = new ProjectFileVO();
        vo.setId(file.getId());
        vo.setProjectId(file.getProjectId());
        vo.setPath(file.getPath());
        vo.setIsDir(file.getIsDir()); // 这里先保留数据库的值
        vo.setContent(file.getContent());
        vo.setCreateTime(file.getCreateTime());
        vo.setUpdateTime(file.getUpdateTime());
        return vo;
    }

    private static String getParentPath(String path) {
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash == -1) {
            return null;
        }
        return path.substring(0, lastSlash);
    }

}
