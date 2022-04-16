package com.ihomefnt.skureplace;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fangyuan Wang
 */

@Slf4j
public class Main {

    public static void main(String[] args) {
        skuRead();
    }


    @SneakyThrows
    public static void skuRead() {
        String replaceSkuConfigJsonPath = FileUtil.getPath() + File.separator + "replace-sku-config.json";
        String excelPath = FileUtil.getPath() + File.separator + "sku.xlsx";

        JSONReader replaceSkuConfigJsonReader = getJsonReader(replaceSkuConfigJsonPath);
        ReplaceSkuConfig replaceSkuConfig = replaceSkuConfigJsonReader.readObject(ReplaceSkuConfig.class);
        Set<ReplaceSkuConfig.Sku> originSkuSet = replaceSkuConfig.getConfigList();
        Map<Long, ReplaceSkuConfig.Sku> originMap = originSkuSet
                .stream()
                .collect(Collectors.toMap(ReplaceSkuConfig.Sku::getSkuId, Function.identity()));

        EasyExcel.read(excelPath, ExcelReplaceSku.class, new PageReadListener<ExcelReplaceSku>(dataList -> {
            for (ExcelReplaceSku excelReplaceSku : dataList) {
                if (originMap.containsKey(excelReplaceSku.getSkuId())) {
                    Set<Long> replaceSkuIds = originMap.get(excelReplaceSku.getSkuId()).getReplaceSkuIds();
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId1())) {
                        replaceSkuIds.add(excelReplaceSku.getReplaceSkuId1());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId2())) {
                        replaceSkuIds.add(excelReplaceSku.getReplaceSkuId2());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId3())) {
                        replaceSkuIds.add(excelReplaceSku.getReplaceSkuId3());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId4())) {
                        replaceSkuIds.add(excelReplaceSku.getReplaceSkuId4());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId5())) {
                        replaceSkuIds.add(excelReplaceSku.getReplaceSkuId5());

                    }
                } else {
                    ReplaceSkuConfig.Sku sku = new ReplaceSkuConfig.Sku();
                    sku.setSkuId(excelReplaceSku.getSkuId());
                    Set<Long> skuIds = new HashSet<>();
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId1())) {
                        skuIds.add(excelReplaceSku.getReplaceSkuId1());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId2())) {
                        skuIds.add(excelReplaceSku.getReplaceSkuId2());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId3())) {
                        skuIds.add(excelReplaceSku.getReplaceSkuId3());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId4())) {
                        skuIds.add(excelReplaceSku.getReplaceSkuId4());

                    }
                    if (Objects.nonNull(excelReplaceSku.getReplaceSkuId5())) {
                        skuIds.add(excelReplaceSku.getReplaceSkuId5());

                    }
                    sku.setReplaceSkuIds(skuIds);

                    originMap.put(excelReplaceSku.getSkuId(), sku);
                }

                log.info("读取到一条数据{}", JSON.toJSONString(replaceSkuConfig));
            }
        })).sheet().headRowNumber(2).doRead();

        Set<ReplaceSkuConfig.Sku> skuSet = new HashSet<>(originMap.values());
        replaceSkuConfig.setConfigList(skuSet);

        File file = new File(replaceSkuConfigJsonPath);

        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        write.write(JSON.toJSONString(replaceSkuConfig));
        write.flush();
        write.close();


    }

    private static JSONReader getJsonReader(String srcPath) {
        JSONReader jsonReader = null;
        try {
            FileReader fileReader = new FileReader(srcPath);
            jsonReader = new JSONReader(fileReader);
        } catch (FileNotFoundException e) {
            log.error("loadTestData: 读取失败:", e);
            e.printStackTrace();
        }
        return jsonReader;
    }
}
