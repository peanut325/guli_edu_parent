package com.atguigu.statisticsservice.controller;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-03-02
 */
@RestController
@RequestMapping("/statisticsservice/statistics-daily")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    // 保存当天的统计数据
    @PostMapping("countRegister/{day}")
    public ResultEntity countRegister(@PathVariable String day) {
        statisticsDailyService.countRegister(day);
        return ResultEntity.ok();
    }

    // 显示统计数据
    @GetMapping("getData/{type}/{begin}/{end}")
    public ResultEntity getData(
            @PathVariable String type,
            @PathVariable String begin,
            @PathVariable String end
    ) {
        Map<String, Object> map = statisticsDailyService.getData(type, begin, end);
        return ResultEntity.ok().data(map);
    }

}

