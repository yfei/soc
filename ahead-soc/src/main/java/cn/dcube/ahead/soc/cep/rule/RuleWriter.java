/*
 * Copyright (c) cecgw 2013 All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.dcube.ahead.soc.cep.rule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.dcube.ahead.soc.util.Constant;
import cn.dcube.goku.commons.util.PathUtil;
import cn.dcube.goku.commons.util.StringUtils;

/**
 * 
 * 描述：
 * <p>
 * 更新规则。
 * </p>
 * 创建日期：2013-12-5 下午4:53:13<br>
 * 
 * @author：dai_hyan<br>
 * @update：$Date$<br>
 * @version：$Revision$<br>
 * @since 3.1.0
 */
public class RuleWriter {

	private static final Logger kernelLog = LogManager.getLogger("kernelFile");

	private static final Logger errorLog = LogManager.getLogger("errorFile");

	public static int saveRuleList(Map<Integer, CB_RuleObject> ruleobjects,
			String type) {
		String fpath = PathUtil.getFilePathFromConf("rule-analysis.ini");
		String ofpath = fpath.substring(0, fpath.indexOf("rule-analysis.ini"))
				+ "oldrule-analysis.ini";
		if (Constant.RULE_STATISTICS.equals(type)) {
			fpath = PathUtil.getFilePathFromConf("rule-statistics.ini");
			ofpath = fpath.substring(0, fpath.indexOf("rule-statistics.ini"))
					+ "oldrule-statistics.ini";
		}
		File file1 = new File(fpath);
		File file2 = new File(ofpath);
		// 若旧文件存在，删除旧文件
		file2.delete();
		boolean flag = file1.renameTo(file2);
		if (flag) {
			CB_RuleObject ruleobj = null;
			FileWriter fw = null;
			try {
				fw = new FileWriter(fpath, true);
				for (Integer i : ruleobjects.keySet()) {
					ruleobj = ruleobjects.get(i);
					saveRule(fw, ruleobj);
				}
				fw.flush();
			} catch (IOException e) {
				errorLog.error("write rule file error:{}", e);
				return 1;
			} finally {
				if (null != fw) {
					try {
						fw.close();
					} catch (IOException e) {
						errorLog.error("", e);
						return 1;
					}
				}
			}
		}

		return 0;
	}

	public static int saveAddRule(CB_RuleObject ruleobj, String type) {
		String fpath = PathUtil.getFilePathFromConf("rule-analysis.ini");
		if (Constant.RULE_STATISTICS.equals(type)) {
			fpath = PathUtil.getFilePathFromConf("rule-statistics.ini");
		}
		try {
			FileWriter fw = new FileWriter(fpath, true);
			saveRule(fw, ruleobj);
			fw.close();
		} catch (Exception e) {
			errorLog.error("write rule file error:{}", e);
			return 1;
		}
		return 0;
	}

	private static void saveRule(FileWriter fw, CB_RuleObject ruleobj)
			throws IOException {
		fw.write("\r\n"); // 输入空行
		String content = "[" + ruleobj.getDiscrib() + "]\r\n";
		fw.write(content);
		content = "id=" + ruleobj.getRuleid() + "\r\n";
		fw.write(content);
		content = "type=" + ruleobj.getType() + "\r\n";
		fw.write(content);
		List<String> preid = ruleobj.getPreid();
		content = "preid=";
		// 前置规则使用|进行分割
		if (preid.size() > 1) {
			for (int j = 0; j < preid.size(); j++) {
				content += preid.get(j).toString();
				content += ",";
			}
			content = content.substring(0, content.length() - 1);
		} else if (preid.size() == 1) { // 只有1个前置规则不需要,分割
			content += preid.get(0).toString();
		}
		fw.write(content + "\r\n");
		content = "level=" + ruleobj.getLevel() + "\r\n";
		fw.write(content);
		content = "subclass=" + ruleobj.getSubclass() + "\r\n";
		fw.write(content);
		content = "family=" + ruleobj.getFamily() + "\r\n";
		fw.write(content);
		content = "isGene=" + ruleobj.getIsGene() + "\r\n";
		fw.write(content);
		if(null==ruleobj.getTag()||"".equals(ruleobj.getTag())){
			// 默认为轻度可疑
			ruleobj.setTag(Constant.TAG_QDKY);
		}
		content = "tag=" + ruleobj.getTag() + "\r\n";
		fw.write(content);
		if (ruleobj.getColmapping() != null) {
			content = "colmapping=" + ruleobj.getColmapping() + "\r\n";
			fw.write(content);
		}
		// 累加属性
		if (ruleobj.getAccumulative() == 1) {
			content = "accumulative=" + ruleobj.getAccumulative() + "\r\n";
			fw.write(content);

			content = "accumulationCol=" + ruleobj.getAccumulationCol()
					+ "\r\n";
			fw.write(content);
			content = "accumulationUnit=" + ruleobj.getAccumulationUnit()
					+ "\r\n";
			fw.write(content);
			if (StringUtils.isNotBlank(ruleobj.getAccumulationGroupkey())) {
				content = "accumulationGroupkey="
						+ ruleobj.getAccumulationGroupkey() + "\r\n";
				fw.write(content);
			}
		}
		List<String> rules = ruleobj.getRules();
		for (int n = 0; n < rules.size(); n++) {
			content = "rule=" + rules.get(n) + "\r\n";
			fw.write(content);
		}
		content = "output=" + ruleobj.getOutput() + "\r\n";
		fw.write(content);
	}

}
