package com.winning.kbms.core.controller;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.winning.domain.kbms.core.NavMenu;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.domain.Page;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.NavMenuService;
import com.winning.kbms.core.service.TreeService;
import com.winning.kbms.core.utils.KeyGenerator;
import com.winning.utils.RandomUIDUtils;

@Controller
@RequestMapping ("/navMenu")
public class NavMenuController extends AbstractTreeController <NavMenu>
{
    private final Logger logger = LoggerFactory.getLogger (getClass ());

    @Resource (name = "navMenuService")
    private NavMenuService navMenuService;

    @Override
    @SuppressWarnings ("unchecked")
    protected TreeService <NavMenu> getTreeService ()
    {
        return (TreeService <NavMenu>) navMenuService;
    }

    @RequestMapping ("/findFunctionsByNodeId.do")
    @ResponseBody
    public JResult findFunctionsByNodeId (@RequestParam ("nodeId") String nodeId)
    {
        Page <NavMenu> page = new Page <NavMenu> ();
        List <NavMenu> functions = this.getChildrenById (nodeId);
        page.setResult (functions);
        page.setTotalCount (functions.size ());
        return new JResult (true, page);
    }

    @RequestMapping ("/functionExecute.do")
    @ResponseBody
    public JResult functionExecute (NavMenu function, @RequestParam ("oper") String oper)
    {
        try
        {
            if (oper.equals (Constants.OPER_ADD))
            {
                function.setId (RandomUIDUtils.getUUID ());
                function.setIconCls ("cog");
                function.setModelId(generateModelId());
                this.add (function);
                return new JResult (true, "新增成功！");
            }
            else if (oper.equals (Constants.OPER_EDIT))
            {
                function.setIconCls ("cog");
                this.update (function);
                return new JResult (true, "修改成功！");
            }
            else if (oper.equals (Constants.OPER_DEL))
            {
                this.remove (function);
                return new JResult (true, "删除成功！");
            }
            else
            {
                throw new ValidationException ("\"oper\"的值必须为：add或edit或del");
            }
        }
        catch (Exception e)
        {
            logger.error (e.getMessage (), e);
            return new JResult (false, e.getMessage ());
        }
    }

    @RequestMapping ("/cutNavMenu.do")
    @ResponseBody
    public void cutNavMenu (@RequestParam ("id") String id, @RequestParam ("parentId") String parentId)
    {
        navMenuService.cutNavMenu (id, parentId);
    }

    @RequestMapping ("/getNavMeunFunWithUserAuth.do")
    @ResponseBody
    public List <NavMenu> getNavMeunFunWithUserAuth (@RequestParam ("id") String id,
                                                     @RequestParam ("userName") String userName)
    {
        return navMenuService.getNavMeunFunWithUserAuth (id, userName);
    }

    @RequestMapping ("/getNavMeunFunWithRoleAuth.do")
    @ResponseBody
    public List <NavMenu> getNavMeunFunWithRoleAuth (@RequestParam ("id") String id,
                                                     @RequestParam ("roleId") String roleId)
    {
        return navMenuService.getNavMeunFunWithRoleAuth (id, roleId);
    }

    @RequestMapping ("/getNavMenuChildrenByIdWithAuth.do")
    @ResponseBody
    public List <NavMenu> getNavMenuChildrenByIdWithAuth (@RequestParam ("id") String id)
    {
        return navMenuService.getNavMenuChildrenByIdWithAuth (id, this.getCurrUser ().getUserName ());
    }

    @RequestMapping ("/getAllNavMenuChildrenByIdWithAuth.do")
    @ResponseBody
    public List <NavMenu> getAllNavMenuChildrenByIdWithAuth (@RequestParam ("id") String id)
    {
        return navMenuService.getAllNavMenuChildrenByIdWithAuth (id, this.getCurrUser ().getUserName ());
    }
    
    @RequestMapping ("/getRuleRootNavMenu.do")
    @ResponseBody
    public List<NavMenu> getRuleRootNavMenu(@RequestParam ("id") String ids){
    	return navMenuService.getRuleRootNavMenu(ids);
    }
    
    /**
     * 
     * @date 2017年3月25日 下午9:17:24
     * @author qiushengming
     * @return String
     * <p>构建模块ID,格式M000</p>
     */
    private String generateModelId(){
        /*  格式化ID   */
        DecimalFormat df=new DecimalFormat("000");
        
        return Constants.NAV_MENU_MODEL_ID_KEY +
                df.format(KeyGenerator
                        .getNextKey(Constants
                                .NAV_MENU_MODEL_ID_KEY_NAME));
    }
    
    @Override
    protected void addValidation(NavMenu obj) throws ValidationException {
        /*  设置MODEL_ID  */
        obj.setModelId(generateModelId());
        super.addValidation(obj);
    }

}
