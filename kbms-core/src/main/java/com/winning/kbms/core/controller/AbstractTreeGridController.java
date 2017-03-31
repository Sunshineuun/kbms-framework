package com.winning.kbms.core.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.domain.kbms.core.BaseTreeNode;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.TreeGridService;
import com.winning.kbms.core.service.TreeService;

public abstract class AbstractTreeGridController <T extends BaseTreeNode> extends AbstractTreeController <T>
{
    public static final String OPER_ADD_ROOT = "addRoot";
    public static final String OPER_ADD_CHILD = "addChild";
    public static final String OPER_ADD_PARENT = "addParent";

    protected abstract TreeGridService <T> getTreeGridService ();

    protected TreeService <T> getTreeService ()
    {
        return getTreeGridService ();
    }

    @ResponseBody
    @RequestMapping ("/tree.do")
    public List <T> list (@RequestParam ("search") boolean search) throws Exception
    {
        Map <String, Object> params = super.getRequestParameters ();

        List <T> list;
        if (search)
            list = getTreeGridService ().searchTree (params);
        else
            list = getTreeGridService ().getChildrenById ((String) params.get ("id"));

        return list;
    }

    @RequestMapping ("/execute.do")
    @ResponseBody
    public JResult execute (T obj, @RequestParam ("oper") String oper) throws Exception
    {
        try
        {
            if (oper.equals (OPER_ADD_ROOT))
            {
                if (StringUtils.isEmpty (obj.getId ()))
                    obj.setId (generateId ());
                if (obj.getUpdateTime () == null)
                    obj.setUpdateTime (new Date ());
                if (obj.getUpdateBy () == null)
                    obj.setUpdateBy (this.getCurrUser ().getUserName ());

                this.add (obj);
                return new JResult (true, "新增成功！");
            }
            else if (oper.equals (OPER_ADD_CHILD))
            {
                if (StringUtils.isEmpty (obj.getId ()))
                    obj.setId (generateId ());
                if (obj.getUpdateTime () == null)
                    obj.setUpdateTime (new Date ());
                if (obj.getUpdateBy () == null)
                    obj.setUpdateBy (this.getCurrUser ().getUserName ());

                this.add (obj);
                return new JResult (true, "新增成功！");
            }
            else if (oper.equals (OPER_ADD_PARENT))
            {
                if (StringUtils.isEmpty (obj.getId ()))
                    obj.setId (generateId ());
                if (obj.getUpdateTime () == null)
                    obj.setUpdateTime (new Date ());
                if (obj.getUpdateBy () == null)
                    obj.setUpdateBy (this.getCurrUser ().getUserName ());

                addParentNode (obj, this.getRequest ().getParameter ("currNodeId"));
                return new JResult (true, "新增成功！");
            }
            else if (oper.equals (Constants.OPER_EDIT))
            {
                if (obj.getUpdateTime () == null)
                    obj.setUpdateTime (new Date ());
                if (obj.getUpdateBy () == null)
                    obj.setUpdateBy (this.getCurrUser ().getUserName ());

                this.update (obj);
                return new JResult (true, "修改成功！");
            }
            else if (oper.equals (Constants.OPER_DEL))
            {
                this.remove (obj);
                return new JResult (true, "删除成功！");
            }
            else
            {
                throw new ValidationException ("\"oper\"的值必须为：add或edit或del或batchDel");
            }
        }
        catch (ValidationException e)
        {
            return new JResult (false, e.getMessage ());
        }
    }
}
