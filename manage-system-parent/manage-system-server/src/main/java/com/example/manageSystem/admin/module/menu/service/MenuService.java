package com.example.manageSystem.admin.module.menu.service;

import com.example.manageSystem.admin.model.Menu;
import com.example.manageSystem.admin.module.menu.dao.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    @Autowired
    MenuDao menuDao;

    //根据角色id查询菜单
    public List<Menu> findByRoleId(Integer roleId){
        return menuDao.findByRoleId(roleId);
    }

    public Map<String,Object> getMenuTree(Integer roleId){
        Map<String, Object> map = new HashMap<>();
        List<Menu> menus = this.getAllMenuTree();
        List<Menu> perms = this.getMenuTreeByRoleId(roleId);
        List<Integer> permsIds = new ArrayList<>();
        for (Menu perm : perms) {
            permsIds.add(perm.getId());
        }
        map.put("menus",menus);
        map.put("permsIds",permsIds);
        return map;
    }

    //根据角色id获取树状菜单
    public List<Menu> getMenuTreeByRoleId(Integer roleId){
        List<Menu> perms = menuDao.findByRoleId(roleId);
        perms = this.parseMenuTree(perms);
        return perms;
    }

    //获取所有菜单的树状菜单
    public List<Menu> getAllMenuTree(){
        List<Menu> menus =menuDao.selectAll();
        menus = this.parseMenuTree(menus);
        return menus;
    }

    /**
     * @方法名: parseMenuTree<br>
     * @描述: 组装菜单<br>
     * @param list 数据库里面获取到的全量菜单列表
     * @return
     */
    public  List<Menu> parseMenuTree(List<Menu> list){
        List<Menu> menuTree = new ArrayList<Menu>();

        // 1、获取第一级节点
        for (Menu menu : list) {
            if(null == menu.getParentId()) {
                menuTree.add(menu);
            }
        }

        // 2、递归获取子节点
        for (Menu parent : menuTree) {
            parent = recursiveTree(parent, list);
        }

        return menuTree;
    }

    public  Menu recursiveTree(Menu parent, List<Menu> list) {
        for (Menu menu : list) {
            if(parent.getId() == menu.getParentId()) {
                menu = recursiveTree(menu, list);
                parent.getChildren().add(menu);
            }
        }
        return parent;
    }

    public List<Menu> findAll() {
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        List<Menu> menus = menuDao.selectAll();
        return menus;
    }


    public Menu findById(Integer id) {
        Menu menu = menuDao.selectByPrimaryKey(id);
        return menu;
    }

    @Transactional
    public boolean add(Menu menu) {
        int i = menuDao.insert(menu);
        return i>0;
    }

    @Transactional
    public boolean edit(Menu menu) {
        int i = 0;
        Menu DbMenu = findById(menu.getId());
        if (DbMenu!=null){
            i = menuDao.updateByPrimaryKey(menu);
        }
        return i>0;
    }


    @Transactional
    public boolean del(Integer id) {
        List<Menu> menus = this.findChildrenMenu(id);
        if (menus!=null||menus.size()>0){
            throw new RuntimeException("该菜单存在子菜单，请先删除子菜单");
        }
        int i = menuDao.deleteByPrimaryKey(id);
        int y = menuDao.delRoleAndMenuByMenuId(id);
        return i+y>0;
    }

    //查询子菜单
    public List<Menu> findChildrenMenu(Integer parentId){
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",parentId);
        List<Menu> menus = menuDao.selectByExample(example);
        return menus;
    }
}
