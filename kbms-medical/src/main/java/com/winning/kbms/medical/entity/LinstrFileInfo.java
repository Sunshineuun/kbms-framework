package com.winning.kbms.medical.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "IL_INSTR_FILE_INFO", schema = "dbo")
public class LinstrFileInfo extends BaseDomain
{

	/**
	 * 售后服务企业名称
	 */
	private String servicesEiNameCn;
	/**
	 * 售后服务地址
	 */
	private String servicesAddress;
	/**
	 * 售后服务联系方式
	 */
	private String servicesTel;

	/**
	 * 企业中文
	 */
	private String eiNameCn;

	/**
	 * 企业中文注册地址
	 */
	private String eiAddressCn;
	/**
	 * 生产地址
	 */
	private String eiMenufactureAddrCn;
	/**
	 * 网址
	 */
	private String eiWebsiteUrl;
	/**
	 * Email
	 */
	private String eiEmail;
	/**
	 * 传真
	 */
	private String eiFax;
	/**
	 * 电话
	 */
	private String eiTel;
	
	/**
	 * 注册地邮
	 */
	private String eiAddressPost;
	/**
	 * 复方制剂组分
	 */
	private String ingPreComponent;
	/**
	 * 单证UUID
	 */
	private String fid;
	/**
	 * 药品注册证号__9206
	 */
	private String regNo9206;
	/**
	 * 境内
	 */
	private String country;
	/**
	 * 省份
	 */
	private String provCode;
	/**
	 * 说明书申报类
	 */
	private Integer applyType;
	/**
	 * 原始药品信息状
	 */
	private Integer swapDataState;
	/**
	 * 方法的局限
	 */
	private String testMethodLimitation;
	/**
	 * 产品性能指标
	 */
	private String productPerfIndex;
	/**
	 * 参考文献
	 */
	private String references;

	private String type;
	/**
	 * 审核结论
	 */
	private Integer result;
	/**
	 * 审核结论注解
	 */
	private String resultAnnotation;
	/**
	 * 签字级别
	 */
	private Integer sign_level;
	/**
	 * 是否是新版本
	 */
	private Integer isNew;
	
	/**
	 * 企业编码
	 */
	private String eiCode;
	/**
	 * 企业名称
	 */
	private String eiName;
	/**
	 * 说明书原始ID
	 */
	private String originalUuid;
	/**
	 * 汉语拼音
	 */
	private String productPinyin;
	/**
	 * 作用类别
	 */
	private String effectCategory;
	/**
	 * 接种人群
	 */
	private String inoculatePerson;
	/**
	 * 药品注册证号
	 */
	private String regNo;
	/**
	 * 原理
	 */
	private String checkPrinciple;

	/**
	 * 生产企业许可证编
	 */
	private String eiMdMcNo;

	/**
	 * 执行标准
	 */
	private String executeStandard;

	/**
	 * 批准文号
	 */
	private String approveDocNo;
	/**
	 * 版本
	 */
	private Integer version_no;
	/**
	 * 版本说明
	 */
	private String editionExplanation;
	/**
	 * 是否处方
	 */
	private Integer isOtc;
	/**
	 * 申请时间
	 */
	private Date applyTime;
	/**
	 * 注册类别
	 */
	private Integer regCategory;
	/**
	 * 放射性核素半衰期
	 */
	private String radionuclideHalfLife;
	/**
	 * 放射性活度和标示时间
	 */
	private Date radioactivityActivity;
	/**
	 * 内辐射吸收剂
	 */
	private String inrad_abs_dos;

	/**
	 * 成份
	 */
	private String component;
	/**
	 * 储存条件
	 */
	private String storeCondition;
	/**
	 * 适用仪器
	 */
	private String suitableInstrument;
	/**
	 * 样本要求
	 */
	private String sampleDemands;
	/**
	 * 方法
	 */
	private String test_method;
	/**
	 * 参考值
	 */
	private String referenceValue;
	/**
	 * 结果解释
	 */
	private String testResultExplain;

	/**
	 * 适应
	 */
	private String indication;

	/**
	 * 禁忌
	 */
	private String taboo;

	/**
	 * 注意事项
	 */
	private String attentionItem;

	/**
	 * 孕妇及哺乳期妇女用药
	 */
	private String womanMedicine;

	/**
	 * 儿童用药
	 */
	private String childrenMedicine;

	/**
	 * 老年用药
	 */
	private String agednessMedicine;

	/**
	 * 药物相互作用
	 */
	private String medicineInteracts;

	/**
	 * 药物过量
	 */
	private String medicineBellyful;
	/**
	 * 临床试验
	 */
	private String clinicTest;

	/**
	 * 药理毒理
	 */
	private String pharmacologyPoisons;

	/**
	 * 药代动力
	 */
	private String medicineDynamics;

	/**
	 * 贮藏
	 */
	private String store;

	/**
	 * 包装
	 */
	private String casing;

	/**
	 * 有效
	 */
	private String validPeriod;

	/**
	 * 警示
	 */
	private String caution;

	/**
	 * 药品通用名称
	 */
	private String productNameCn;

	/**
	 * 药品名称
	 */
	private String tradeName;
	/**
	 * 英文名称
	 */
	private String productNameEn;
	/**
	 * 化学名称
	 */
	private String productNameChemical;
	/**
	 * 化学结构
	 */
	private String structuralFormulaPic;
	/**
	 * 分子
	 */
	private String structuralFormula;
	/**
	 * 分子
	 */
	private String molecularWeight;

	/**
	 * 性状
	 */
	private String quality;

	/**
	 * 规格
	 */
	private String specification;

	/**
	 * 用法用量
	 */
	private String usageDosage;

	/**
	 * 不良反应
	 */
	private String badReaction;
	/**
	 * 药品标识
	 */
	private String medicineMark;

	/**
	 * 说明书标
	 */
	private String instructionsNname;

	/**
	 * 修订日期
	 */
	private Date emendDate;

	/**
	 * 核准日期
	 */
	private Date approveDate;
	/**
	 * 剂型
	 */
	private String drugForm;
	private String pinyin;

	@Column(name = "APPROVE_DATE")
	public Date getApproveDate()
	{
		return approveDate;
	}

	public void setApproveDate(Date approveDate)
	{
		this.approveDate = approveDate;
	}

	@Column(name = "EMEND_DATE")
	public Date getEmendDate()
	{
		return emendDate;
	}

	public void setEmendDate(Date emendDate)
	{
		this.emendDate = emendDate;
	}

	@Column(name = "INSTRUCTIONS_NAME")
	public String getInstructionsNname()
	{
		return instructionsNname;
	}

	public void setInstructionsNname(String instructionsNname)
	{
		this.instructionsNname = instructionsNname;
	}

	@Column(name = "MEDICINE_MARK")
	public String getMedicineMark()
	{
		return medicineMark;
	}

	public void setMedicineMark(String medicineMark)
	{
		this.medicineMark = medicineMark;
	}

	@Column(name = "CAUTION")
	public String getCaution()
	{
		return caution;
	}

	public void setCaution(String caution)
	{
		this.caution = caution;
	}

	@Column(name = "PRODUCT_NAME_CN")
	public String getProductNameCn()
	{
		return productNameCn;
	}

	public void setProductNameCn(String productNameCn)
	{
		this.productNameCn = productNameCn;
	}

	@Column(name = "TRADE_NAME")
	public String getTradeName()
	{
		return tradeName;
	}

	public void setTradeName(String tradeName)
	{
		this.tradeName = tradeName;
	}

	@Column(name = "PRODUCT_NAME_EN")
	public String getProductNameEn()
	{
		return productNameEn;
	}

	public void setProductNameEn(String productNameEn)
	{
		this.productNameEn = productNameEn;
	}

	@Column(name = "PRODUCT_NAME_CHEMICAL")
	public String getProductNameChemical()
	{
		return productNameChemical;
	}

	public void setProductNameChemical(String productNameChemical)
	{
		this.productNameChemical = productNameChemical;
	}

	@Column(name = "STRUCTURAL_FORMULA_PIC")
	public String getStructuralFormulaPic()
	{
		return structuralFormulaPic;
	}

	public void setStructuralFormulaPic(String structuralFormulaPic)
	{
		this.structuralFormulaPic = structuralFormulaPic;
	}

	@Column(name = "STRUCTURAL_FORMULA")
	public String getStructuralFormula()
	{
		return structuralFormula;
	}

	public void setStructuralFormula(String structuralFormula)
	{
		this.structuralFormula = structuralFormula;
	}

	@Column(name = "MOLECULAR_WEIGHT")
	public String getMolecularWeight()
	{
		return molecularWeight;
	}

	public void setMolecularWeight(String molecularWeight)
	{
		this.molecularWeight = molecularWeight;
	}

	@Column(name = "QUALITY")
	public String getQuality()
	{
		return quality;
	}

	public void setQuality(String quality)
	{
		this.quality = quality;
	}

	@Column(name = "INDICATION")
	public String getIndication()
	{
		return indication;
	}

	public void setIndication(String indication)
	{
		this.indication = indication;
	}

	@Column(name = "SPECIFICATION")
	public String getSpecification()
	{
		return specification;
	}

	public void setSpecification(String specification)
	{
		this.specification = specification;
	}

	@Column(name = "USAGE_DOSAGE")
	public String getUsageDosage()
	{
		return usageDosage;
	}

	public void setUsageDosage(String usageDosage)
	{
		this.usageDosage = usageDosage;
	}

	@Column(name = "BAD_REACTION")
	public String getBadReaction()
	{
		return badReaction;
	}

	public void setBadReaction(String badReaction)
	{
		this.badReaction = badReaction;
	}

	@Column(name = "TABOO")
	public String getTaboo()
	{
		return taboo;
	}

	public void setTaboo(String taboo)
	{
		this.taboo = taboo;
	}

	@Column(name = "ATTENTION_ITEM")
	public String getAttentionItem()
	{
		return attentionItem;
	}

	public void setAttentionItem(String attentionItem)
	{
		this.attentionItem = attentionItem;
	}

	@Column(name = "WOMAN_MEDICINE")
	public String getWomanMedicine()
	{
		return womanMedicine;
	}

	public void setWomanMedicine(String womanMedicine)
	{
		this.womanMedicine = womanMedicine;
	}

	@Column(name = "CHILDREN_MEDICINE")
	public String getChildrenMedicine()
	{
		return childrenMedicine;
	}

	public void setChildrenMedicine(String childrenMedicine)
	{
		this.childrenMedicine = childrenMedicine;
	}

	@Column(name = "AGEDNESS_MEDICINE")
	public String getAgednessMedicine()
	{
		return agednessMedicine;
	}

	public void setAgednessMedicine(String agednessMedicine)
	{
		this.agednessMedicine = agednessMedicine;
	}

	@Column(name = "MEDICINE_INTERACTS")
	public String getMedicineInteracts()
	{
		return medicineInteracts;
	}

	public void setMedicineInteracts(String medicineInteracts)
	{
		this.medicineInteracts = medicineInteracts;
	}

	@Column(name = "MEDICINE_BELLYFUL")
	public String getMedicineBellyful()
	{
		return medicineBellyful;
	}

	public void setMedicineBellyful(String medicineBellyful)
	{
		this.medicineBellyful = medicineBellyful;
	}

	@Column(name = "CLINIC_TEST")
	public String getClinicTest()
	{
		return clinicTest;
	}

	public void setClinicTest(String clinicTest)
	{
		this.clinicTest = clinicTest;
	}

	@Column(name = "PHARMACOLOGY_POISONS")
	public String getPharmacologyPoisons()
	{
		return pharmacologyPoisons;
	}

	public void setPharmacologyPoisons(String pharmacologyPoisons)
	{
		this.pharmacologyPoisons = pharmacologyPoisons;
	}

	@Column(name = "medicine_dynamics")
	public String getMedicineDynamics()
	{
		return medicineDynamics;
	}

	public void setMedicineDynamics(String medicineDynamics)
	{
		this.medicineDynamics = medicineDynamics;
	}

	@Column(name = "STORE", length = 200)
	public String getStore()
	{
		return store;
	}

	public void setStore(String store)
	{
		this.store = store;
	}

	@Column(name = "CASING")
	public String getCasing()
	{
		return casing;
	}

	public void setCasing(String casing)
	{
		this.casing = casing;
	}

	@Column(name = "VALID_PERIOD", length = 20)
	public String getValidPeriod()
	{
		return validPeriod;
	}

	public void setValidPeriod(String validPeriod)
	{
		this.validPeriod = validPeriod;
	}

	@Column(name = "EXECUTE_STANDARD")
	public String getExecuteStandard()
	{
		return executeStandard;
	}

	public void setExecuteStandard(String executeStandard)
	{
		this.executeStandard = executeStandard;
	}

	@Column(name = "APPROVE_DOC_NO")
	public String getApproveDocNo()
	{
		return approveDocNo;
	}

	public void setApproveDocNo(String approveDocNo)
	{
		this.approveDocNo = approveDocNo;
	}

	@Column(name = "VERSION_NO")
	public Integer getVersion_no()
	{
		return version_no;
	}

	public void setVersion_no(Integer version_no)
	{
		this.version_no = version_no;
	}

	@Column(name = "edition_explanation", length = 200)
	public String getEditionExplanation()
	{
		return editionExplanation;
	}

	public void setEditionExplanation(String editionExplanation)
	{
		this.editionExplanation = editionExplanation;
	}

	@Column(name = "IS_OTC")
	public Integer getIsOtc()
	{
		return isOtc;
	}

	public void setIsOtc(Integer isOtc)
	{
		this.isOtc = isOtc;
	}

	@Column(name = "APPLY_TIME")
	public Date getApplyTime()
	{
		return applyTime;
	}

	public void setApplyTime(Date applyTime)
	{
		this.applyTime = applyTime;
	}

	@Column(name = "REG_CATEGORY")
	public Integer getRegCategory()
	{
		return regCategory;
	}

	public void setRegCategory(Integer regCategory)
	{
		this.regCategory = regCategory;
	}

	@Column(name = "RADIONUCLIDE_HALF_LIFE", length = 20)
	public String getRadionuclideHalfLife()
	{
		return radionuclideHalfLife;
	}

	public void setRadionuclideHalfLife(String radionuclideHalfLife)
	{
		this.radionuclideHalfLife = radionuclideHalfLife;
	}

	@Column(name = "RADIOACTIVITY_ACTIVITY")
	public Date getRadioactivityActivity()
	{
		return radioactivityActivity;
	}

	public void setRadioactivityActivity(Date radioactivityActivity)
	{
		this.radioactivityActivity = radioactivityActivity;
	}

	@Column(name = "INRAD_ABS_DOS")
	public String getInrad_abs_dos()
	{
		return inrad_abs_dos;
	}

	public void setInrad_abs_dos(String inrad_abs_dos)
	{
		this.inrad_abs_dos = inrad_abs_dos;
	}

	@Column(name = "COMPONENT", length = 200)
	public String getComponent()
	{
		return component;
	}

	public void setComponent(String component)
	{
		this.component = component;
	}

	@Column(name = "STORE_CONDITION", length = 200)
	public String getStoreCondition()
	{
		return storeCondition;
	}

	public void setStoreCondition(String storeCondition)
	{
		this.storeCondition = storeCondition;
	}

	@Column(name = "SUITABLE_INSTRUMENT")
	public String getSuitableInstrument()
	{
		return suitableInstrument;
	}

	public void setSuitableInstrument(String suitableInstrument)
	{
		this.suitableInstrument = suitableInstrument;
	}

	@Column(name = "SAMPLE_DEMANDS")
	public String getSampleDemands()
	{
		return sampleDemands;
	}

	public void setSampleDemands(String sampleDemands)
	{
		this.sampleDemands = sampleDemands;
	}

	@Column(name = "TEST_METHOD")
	public String getTest_method()
	{
		return test_method;
	}

	public void setTest_method(String test_method)
	{
		this.test_method = test_method;
	}

	@Column(name = "REFERENCE_VALUE", length = 20)
	public String getReferenceValue()
	{
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue)
	{
		this.referenceValue = referenceValue;
	}

	@Column(name = "TEST_RESULT_EXPLAIN", length = 200)
	public String getTestResultExplain()
	{
		return testResultExplain;
	}

	public void setTestResultExplain(String testResultExplain)
	{
		this.testResultExplain = testResultExplain;
	}

	public String getTestMethodLimitation()
	{
		return testMethodLimitation;
	}

	public void setTestMethodLimitation(String testMethodLimitation)
	{
		this.testMethodLimitation = testMethodLimitation;
	}

	@Column(name = "PRODUCT_PERF_INDEX")
	public String getProductPerfIndex()
	{
		return productPerfIndex;
	}

	public void setProductPerfIndex(String productPerfIndex)
	{
		this.productPerfIndex = productPerfIndex;
	}

	@Column(name = "REFERENCE")
	public String getReferences()
	{
		return references;
	}

	public void setReferences(String references)
	{
		this.references = references;
	}

	@Column(name = "TYPE")
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Column(name = "RESULT")
	public Integer getResult()
	{
		return result;
	}

	public void setResult(Integer result)
	{
		this.result = result;
	}

	@Column(name = "RESULT_ANNOTATION")
	public String getResultAnnotation()
	{
		return resultAnnotation;
	}

	public void setResultAnnotation(String resultAnnotation)
	{
		this.resultAnnotation = resultAnnotation;
	}

	@Column(name = "SIGN_LEVEL")
	public Integer getSign_level()
	{
		return sign_level;
	}

	public void setSign_level(Integer sign_level)
	{
		this.sign_level = sign_level;
	}

	@Column(name = "IS_NEW")
	public Integer getIsNew()
	{
		return isNew;
	}

	public void setIsNew(Integer isNew)
	{
		this.isNew = isNew;
	}

	@Column(name = "EI_CODE", length = 64)
	public String getEiCode()
	{
		return eiCode;
	}

	public void setEiCode(String eiCode)
	{
		this.eiCode = eiCode;
	}

	@Column(name = "EI_NAME")
	public String getEiName()
	{
		return eiName;
	}

	public void setEiName(String eiName)
	{
		this.eiName = eiName;
	}

	@Column(name = "ORIGINAL_UUID")
	public String getOriginalUuid()
	{
		return originalUuid;
	}

	public void setOriginalUuid(String originalUuid)
	{
		this.originalUuid = originalUuid;
	}

	@Column(name = "PRODUCT_PINYIN", length = 50)
	public String getProductPinyin()
	{
		return productPinyin;
	}

	public void setProductPinyin(String productPinyin)
	{
		this.productPinyin = productPinyin;
	}

	@Column(name = "EFFECT_CATEGORY", length = 200)
	public String getEffectCategory()
	{
		return effectCategory;
	}

	public void setEffectCategory(String effectCategory)
	{
		this.effectCategory = effectCategory;
	}

	@Column(name = "INOCULATE_PERSON")
	public String getInoculatePerson()
	{
		return inoculatePerson;
	}

	public void setInoculatePerson(String inoculatePerson)
	{
		this.inoculatePerson = inoculatePerson;
	}

	@Column(name = "REG_NO")
	public String getRegNo()
	{
		return regNo;
	}

	public void setRegNo(String regNo)
	{
		this.regNo = regNo;
	}

	@Column(name = "CHECK_PRINCIPLE")
	public String getCheckPrinciple()
	{
		return checkPrinciple;
	}

	public void setCheckPrinciple(String checkPrinciple)
	{
		this.checkPrinciple = checkPrinciple;
	}

	@Column(name = "EI_MD_MC_NO")
	public String getEiMdMcNo()
	{
		return eiMdMcNo;
	}

	public void setEiMdMcNo(String eiMdMcNo)
	{
		this.eiMdMcNo = eiMdMcNo;
	}

	@Column(name = "SERVICES_EI_NAME_CN")
	public String getServicesEiNameCn()
	{
		return servicesEiNameCn;
	}

	public void setServicesEiNameCn(String servicesEiNameCn)
	{
		this.servicesEiNameCn = servicesEiNameCn;
	}

	@Column(name = "SERVICES_ADDRESS")
	public String getServicesAddress()
	{
		return servicesAddress;
	}

	public void setServicesAddress(String servicesAddress)
	{
		this.servicesAddress = servicesAddress;
	}

	@Column(name = "SERVICES_TEL")
	public String getServicesTel()
	{
		return servicesTel;
	}

	public void setServicesTel(String servicesTel)
	{
		this.servicesTel = servicesTel;
	}

	@Column(name = "EI_NAME_CN", length = 200)
	public String getEiNameCn()
	{
		return eiNameCn;
	}

	public void setEiNameCn(String eiNameCn)
	{
		this.eiNameCn = eiNameCn;
	}

	@Column(name = "EI_ADDRESS_CN")
	public String getEiAddressCn()
	{
		return eiAddressCn;
	}

	public void setEiAddressCn(String eiAddressCn)
	{
		this.eiAddressCn = eiAddressCn;
	}

	@Column(name = "EI_MENUFACTURE_ADDR_CN")
	public String getEiMenufactureAddrCn()
	{
		return eiMenufactureAddrCn;
	}

	public void setEiMenufactureAddrCn(String eiMenufactureAddrCn)
	{
		this.eiMenufactureAddrCn = eiMenufactureAddrCn;
	}

	@Column(name = "EI_WEBSITE_URL")
	public String getEiWebsiteUrl()
	{
		return eiWebsiteUrl;
	}

	public void setEiWebsiteUrl(String eiWebsiteUrl)
	{
		this.eiWebsiteUrl = eiWebsiteUrl;
	}

	@Column(name = "EI_EMAIL")
	public String getEiEmail()
	{
		return eiEmail;
	}

	public void setEiEmail(String eiEmail)
	{
		this.eiEmail = eiEmail;
	}

	@Column(name = "EI_FAX")
	public String getEiFax()
	{
		return eiFax;
	}

	public void setEiFax(String eiFax)
	{
		this.eiFax = eiFax;
	}

	@Column(name = "EI_TEL")
	public String getEiTel()
	{
		return eiTel;
	}

	public void setEiTel(String eiTel)
	{
		this.eiTel = eiTel;
	}

	@Column(name = "EI_ADDRESS_POST")
	public String getEiAddressPost()
	{
		return eiAddressPost;
	}

	public void setEiAddressPost(String eiAddressPost)
	{
		this.eiAddressPost = eiAddressPost;
	}

	@Column(name = "ING_PRE_COMPONENT", length = 200)
	public String getIngPreComponent()
	{
		return ingPreComponent;
	}

	public void setIngPreComponent(String ingPreComponent)
	{
		this.ingPreComponent = ingPreComponent;
	}

	@Column(name = "FID")
	public String getFid()
	{
		return fid;
	}

	public void setFid(String fid)
	{
		this.fid = fid;
	}

	@Column(name = "REG_NO_9206")
	public String getRegNo9206()
	{
		return regNo9206;
	}

	public void setRegNo9206(String regNo9206)
	{
		this.regNo9206 = regNo9206;
	}

	@Column(name = "COUNTRY")
	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	@Column(name = "PROV_CODE")
	public String getProvCode()
	{
		return provCode;
	}

	public void setProvCode(String provCode)
	{
		this.provCode = provCode;
	}

	@Column(name = "APPLY_TYPE")
	public Integer getApplyType()
	{
		return applyType;
	}

	public void setApplyType(Integer applyType)
	{
		this.applyType = applyType;
	}

	@Column(name = "SWAP_DATA_STATE")
	public Integer getSwapDataState()
	{
		return swapDataState;
	}

	public void setSwapDataState(Integer swapDataState)
	{
		this.swapDataState = swapDataState;
	}

	@Column(name = "DRUG_FORM")
	public String getDrugForm()
	{
		return drugForm;
	}

	public void setDrugForm(String drugForm)
	{
		this.drugForm = drugForm;
	}

	@Column(name = "PIN_YIN")
	public String getPinyin()
	{
		return pinyin;
	}
	
	public void setPinyin(String pinyin)
	{
		this.pinyin = pinyin;
	}

}
