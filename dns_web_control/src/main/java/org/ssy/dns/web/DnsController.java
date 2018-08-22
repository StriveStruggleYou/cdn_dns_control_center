package org.ssy.dns.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssy.share.CommonUtil;
import org.ssy.vo.DnsVO;
import org.ssy.vo.TableColumn;

/**
 * Created by manager on 2018/8/21.
 */
@Controller
@RequestMapping("/dns")
public class DnsController {

  @RequestMapping("/list")
  @ResponseBody
  public Object listDns() {
    return "success";
  }

  @RequestMapping("/add")
  @ResponseBody
  public Object addDns(DnsVO dnsVO) {
    //字段类型校验
    int size = CommonUtil.dns.size();
    String key = dnsVO.getDomainPrefix() + ".yidaren.top";
    dnsVO.setId(size + 1);
    CommonUtil.dns.put(key, dnsVO);
    return "success";
  }

  @RequestMapping("/edit")
  @ResponseBody
  public Object editDns(DnsVO dnsVO) {
    //字段类型校验
    String key = dnsVO.getDomainPrefix() + ".yidaren.top";
    if (CommonUtil.getDnsInfo(key) == null) {
      int size = CommonUtil.dns.size();
//      String key = dnsVO.getDomainPrefix() + ".yidaren.top";
      dnsVO.setId(size + 1);
      CommonUtil.dns.put(key, dnsVO);
    } else {
      CommonUtil.dns.put(key, dnsVO);
    }
    return "success";
  }

  @RequestMapping("/delete")
  @ResponseBody
  public Object deleteDns() {
    return "success";
  }

  @RequestMapping("/rows_json")
  @ResponseBody
  public Object rows_json(@RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "0") Integer size) {
    List<DnsVO> list = new ArrayList<>();
    for (String strKey : CommonUtil.dns.keySet()) {
      list.add(CommonUtil.dns.get(strKey));
    }
//    CommonUtil.dns.size()
//    DnsVO dnsVO = new DnsVO();
//    dnsVO.setDnsStatus(0);
//    dnsVO.setDnsType("A");
//    dnsVO.setDomainIps("192.168.1.100");
//    dnsVO.setDomainPrefix("*.server");
//    dnsVO.setTtlValue(360);
//    list.add(dnsVO);
    return list;
  }

  @RequestMapping("/columns_json")
  @ResponseBody
  public Object columns_json() {
    final List<TableColumn> list = new ArrayList<TableColumn>();
    ReflectionUtils.doWithFields(DnsVO.class, new FieldCallback() {
      public void doWith(Field field) throws IllegalArgumentException,
          IllegalAccessException {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setName(field.getName());
        tableColumn.setTitle(field.getName());
        list.add(tableColumn);
      }
    });
    return list;
  }

}
