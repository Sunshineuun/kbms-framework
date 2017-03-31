package com.winning.kbms.medical.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.winning.kbms.medical.entity.LinstrFileInfo;
import com.winning.kbms.medical.service.DrugService;
@Service
public class DrugServiceImpl implements DrugService {
	
	
	@Override
	public Map<String,Object> findLinstrFileInfosWithId(int id) {
		LinstrFileInfo item= null;
		return generateMap(item);
	}
	
	public static Map<String, Object> generateMap(LinstrFileInfo item){
		Map<String, Object> drugMap = new LinkedHashMap<String, Object>();
		if (item != null)
		{   
			 
			StringUtils.trim(null);
			drugMap.put("药品代码:", StringUtils.trim(item.getId ()+""));
			drugMap.put("药品通用名称:", StringUtils.trim(item.getProductNameCn()));
			drugMap.put("药品英文名称:", StringUtils.trim(item.getProductNameEn()));
			drugMap.put("商品名称:", StringUtils.trim(item.getTradeName()));
			drugMap.put("成份:", StringUtils.trim(item.getComponent()));
			drugMap.put("适应症:", StringUtils.trim(item.getIndication()));
			drugMap.put("规格:", StringUtils.trim(item.getSpecification()));
			drugMap.put("用法用量:", StringUtils.trim(item.getUsageDosage()));
			drugMap.put("不良反应:", StringUtils.trim(item.getBadReaction()));
			drugMap.put("禁忌:", StringUtils.trim(item.getTaboo()));
			drugMap.put("注意事项:", StringUtils.trim(item.getAttentionItem()));
			drugMap.put("孕妇及哺乳期妇女用药:", StringUtils.trim(item.getWomanMedicine()));
			drugMap.put("儿童用药:", StringUtils.trim(item.getChildrenMedicine()));
			drugMap.put("老年用药:", StringUtils.trim(item.getAgednessMedicine()));
			drugMap.put("药物相互作用:", StringUtils.trim(item.getMedicineInteracts()));
			drugMap.put("药物过量:", StringUtils.trim(item.getMedicineBellyful()));

			// drugMap.put("药理作用:", item.getMedicineBellyful());
			drugMap.put("药理毒理:", StringUtils.trim(item.getPharmacologyPoisons()));
			drugMap.put("药代动力学:", StringUtils.trim(item.getMedicineDynamics()));
			drugMap.put("性状:", StringUtils.trim(item.getQuality()));
			drugMap.put("贮藏:", StringUtils.trim(item.getStore()));
			drugMap.put("包装:", StringUtils.trim(item.getCasing()));
			drugMap.put("有效期:", StringUtils.trim(item.getValidPeriod()));
			drugMap.put("执行标准:", StringUtils.trim(item.getExecuteStandard()));
			drugMap.put("批准文号:", StringUtils.trim(item.getApproveDocNo()));
			drugMap.put("生产企业许可证编号:", StringUtils.trim(item.getEiMdMcNo()));
			// drugMap.put("药物分类:", item.getEiMdMcNo());
			drugMap.put("药物分类:", StringUtils.trim(item.getEiMdMcNo()));
			SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月dd日");
			try
			{
				drugMap.put("核准日期:", s.format(item.getApproveDate()));
				drugMap.put("核准日期:", s.format(item.getEmendDate()));
			}
			catch (Exception e)
			{
			}
		}
		
		return drugMap;
	}

}
