package com.winning.kbms.core.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.winning.domain.kbms.core.ModifyDomain;
import com.winning.kbms.core.cache.CacheLogId;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.exceptions.WarpedException;
import com.winning.kbms.core.service.ManagementService;
import com.winning.utils.GenericsUtils;
import com.winning.utils.RandomUIDUtils;


public abstract class AbstractManagementController<T extends ModifyDomain> extends QueryController {
    protected abstract ManagementService<T> getManagementService();


    /**
     * 操作单条记录
     * 
     * @throws Exception
     */
    @RequestMapping("/execute.do")
    @ResponseBody
    public JResult execute(T obj, @RequestParam("oper") String oper)
        throws Exception {
        try {
        	if(this.getManagementService().isKnowledgeReviewing(obj,this.getRequest(),oper)){
        		return new JResult(false, "该知识点正在审核中，无法重复操作！");
        	}
        	
            if (oper.equals(Constants.OPER_ADD)) {
                HttpServletRequest request = this.getRequest();
                String addReduction = request.getParameter("addReduction");
                if(StringUtils.equals(addReduction, "true")){
                    String[] ids =this.getManagementService().queryDuplicate(obj);
                    if(ArrayUtils.isNotEmpty(ids)){
                        return new JResult(false, ids);
                    }
                }
                addValidation(obj);
                if (StringUtils.isEmpty(obj.getId()))
                    obj.setId(generateId());
                if (obj.getUpdateTime() == null)
                    obj.setUpdateTime(new Date());
                if (obj.getUpdateBy() == null)
                    obj.setUpdateBy(this.getCurrUser().getUserName());
                addWarped(obj);
                this.add(obj);
                return new JResult(true, "新增成功！");
            }
            else if (oper.equals(Constants.OPER_EDIT)) {
                editValidation(obj);
                if (obj.getUpdateTime() == null)
                    obj.setUpdateTime(new Date());
                if (obj.getUpdateBy() == null)
                    obj.setUpdateBy(this.getCurrUser().getUserName());

                editWarped(obj);
                this.update(obj);
                return new JResult(true, "修改成功！");
            }
            else if (oper.equals(Constants.OPER_DEL)) {
                delValidation(obj);
                this.deleteById(obj.getId());
                return new JResult(true, "删除成功！");

            }
            else if (oper.equals(Constants.OPER_BATCH_DEL)) {
                HttpServletRequest request = this.getRequest();
                String idStr = request.getParameter("ids");
                if (StringUtils.isBlank(idStr))
                    idStr = request.getParameter("id");
                if (StringUtils.isBlank(idStr))
                    throw new ValidationException("删除的id或ids为空！");
                String[] ids = StringUtils.split(idStr, ",");
                List<T> list = new ArrayList<T>(ids.length);
                Class<T> clazz = getDomainClass();
                T domain = null;
                for (String id : ids) {
                    domain = (T) clazz.newInstance();
                    domain.setId(id);
                    list.add(domain);
                }

                this.getManagementService().delete(list);
                return new JResult(true, "删除成功！");
            }
            else if (oper.equals(Constants.OPER_SUBMIT)) {
                HttpServletRequest request = this.getRequest();
                String idStr = request.getParameter("id");
                if (StringUtils.isBlank(idStr))
                    throw new ValidationException("提交的id为空！");

                String[] ids = StringUtils.split(idStr, ",");
                List<T> list = new ArrayList<T>(ids.length);
                Class<T> clazz = getDomainClass();
                T domain = null;
                for (String id : ids) {
                    domain = (T) clazz.newInstance();
                    domain.setId(id);
                    list.add(domain);
                }
                this.getManagementService().submit(list);
                return new JResult(true, "提交成功！");

            }
            else if (oper.equals(Constants.OPER_LOGIC_DEL)) {
                HttpServletRequest request = this.getRequest();
                String idStr = request.getParameter("id");
                if (StringUtils.isBlank(idStr))
                    throw new ValidationException("删除的id或ids为空！");
                String[] ids = StringUtils.split(idStr, ",");
                List<T> list = new ArrayList<T>(ids.length);
                Class<T> clazz = getDomainClass();
                T domain = null;
                for (String id : ids) {
                    domain = (T) clazz.newInstance();
                    domain.setId(id);
                    list.add(domain);
                }
                logicDeleteWarped(list);
                this.logicDelete(list);
                return new JResult(true, "删除成功！");
            }
            else if (oper.equals(Constants.OPER_REDUCTION)) {
                HttpServletRequest request = this.getRequest();
                String[] ids = request.getParameterValues("ids");
                if(ArrayUtils.isEmpty(ids))
                    throw new ValidationException("id或ids为空！");
                List<T> list = new ArrayList<T>(ids.length);
                Class<T> clazz = getDomainClass();
                T domain = null;
                for (String id : ids) {
                    domain = (T) clazz.newInstance();
                    domain.setId(id);
                    list.add(domain);
                }
                this.logicReduction(list);
                return new JResult(true, "还原成功！");
            }
            else {
                throw new ValidationException(
                        "\"oper\"的值必须为：add或edit或del或batchDel或submit或logicDel");
            }
        }
        catch (ValidationException e) {
            return new JResult(false, e.getMessage());
        }
    }


    protected String generateId() {
        return RandomUIDUtils.getUUID();
    }


    protected void addValidation(T obj) throws ValidationException {
    }


    protected void editValidation(T obj) throws ValidationException {
    }


    protected void delValidation(T obj) throws ValidationException {
    }


    protected void addWarped(T obj) throws Exception {
    }


    protected void editWarped(T obj) throws WarpedException {
    }


    public void logicDeleteWarped(List<T> baseDomains) throws ServiceException {
    }


    @SuppressWarnings("unchecked")
    protected Class<T> getDomainClass() {
        return GenericsUtils.getSuperClassGenricType(this.getClass());
    }


    /**
     * 操作多条记录
     * 
     * @return
     * @throws ServiceException
     * @throws ValidationException
     */
    @RequestMapping("/executeList")
    public void executeList(String oper)
        throws ServiceException, ValidationException {
        if (StringUtils.isNotEmpty(oper)) {
            if (oper.equals(Constants.OPER_ADD)) {
                this.addList();
            }
            else if (oper.equals(Constants.OPER_EDIT)) {
                this.updateList();
            }
            else if (oper.equals(Constants.OPER_DEL)) {
                this.deleteList();
            }
            else {
                throw new ServiceException("\"oper\"的值必须为：add或edit或del");
            }
        }
        else {
            throw new ServiceException("必须有请求参数：\"oper\"");
        }
    }


    /**
     * 将传入的参数转换为需要的对象列表
     * 
     * @return
     */
    protected List<T> convertParam2List() {
        return null;
    }


    /**
     * 将传入的参数转换为需要的对象列表
     * 
     * @return
     */
    protected ModifyDomain convertParam2Object() {
        return null;
    };


    /**
     * 新增
     * 
     * @return
     * @throws ServiceException
     */
    public void add(T baseDomain) throws ServiceException {
        this.getManagementService().add(baseDomain);
    }


    /**
     * 更新
     * 
     * @return
     * @throws ServiceException
     */
    public void update(T baseDomain) throws ServiceException {
        this.getManagementService().update(baseDomain);
    }


    /**
     * 提交
     * 
     * @return
     * @throws ServiceException
     */
    public void submit(T baseDomain) throws ServiceException {
        this.getManagementService().submit(baseDomain);
    }


    /**
     * 删除
     * 
     * @return
     * @throws ServiceException
     */
    public void deleteById(String id) throws ServiceException {
        this.getManagementService().deleteById(id);
    }


    /**
     * 逻辑删除
     * 
     * @return
     * @throws ServiceException
     */
    public void logicDelete(List<T> baseDomains) throws ServiceException {
        this.getManagementService().logicDelete(baseDomains);
    }


    /**
     * 批量删除
     * 
     * @return
     * @throws ServiceException
     */
    public void deleteList() throws ServiceException {
        this.getManagementService().delete(this.convertParam2List());
    }


    /**
     * 批量更新
     * 
     * @return
     * @throws ServiceException
     */
    public void updateList() throws ServiceException {
        this.getManagementService().update(this.convertParam2List());
    }


    public void addList() throws ServiceException {
        this.getManagementService().add(this.convertParam2List());
    }

    /**
     * 逻辑删除后恢复数据
     * 
     * @return
     * @throws ServiceException
     */
    public void logicReduction(List<T> baseDomains) throws ServiceException {
        this.getManagementService().logicReduction(baseDomains);
    }

    @RequestMapping("/editLog.do")
    @ResponseBody
    public JResult editLog(@RequestParam("ids") String idStr,
        @RequestParam("changeStatus") String changeStatus,
        @RequestParam("changeDsc") String changeDsc,
        @RequestParam("oper") String oper) {
        if (StringUtils.isBlank(idStr)) {
            throw new ValidationException("获取日志记录ID出错");
        }
        String[] ids = StringUtils.split(idStr, ",");
        Map<String, String> map = new HashMap<String, String>();
        map.put("changeStatus", changeStatus);
        map.put("changeDsc", changeDsc);
        for (String id : ids) {
            CacheLogId.logMap.put(id + oper, map);
        }
        return new JResult(true, null);
    }
}
