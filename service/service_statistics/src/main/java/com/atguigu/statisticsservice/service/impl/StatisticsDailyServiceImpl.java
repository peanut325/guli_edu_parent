package com.atguigu.statisticsservice.service.impl;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.statisticsservice.client.MemberClient;
import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.atguigu.statisticsservice.mapper.StatisticsDailyMapper;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-03-02
 */
@Service
@Transactional(readOnly = true)
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private MemberClient memberClient;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void countRegister(String day) {
        // 先删除表中的数据
        QueryWrapper<StatisticsDaily> statisticsDailyQueryWrapper = new QueryWrapper<>();
        statisticsDailyQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(statisticsDailyQueryWrapper);

        // 远程调用查询数据
        ResultEntity resultEntity = memberClient.countRegister(day);
        Integer countRegister = (Integer) resultEntity.getData().get("countRegister");
        // 使用随机数模拟
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        // 把获取的数据添加到数据库
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(countRegister);
        statisticsDaily.setLoginNum(loginNum);
        statisticsDaily.setVideoViewNum(videoViewNum);
        statisticsDaily.setCourseNum(courseNum);
        statisticsDaily.setDateCalculated(day);

        baseMapper.insert(statisticsDaily);

    }

    @Override
    public Map<String, Object> getData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> statisticsDailyQueryWrapper = new QueryWrapper<>();
        statisticsDailyQueryWrapper.between("date_calculated", begin, end); // 查询在范围内的统计数字
        statisticsDailyQueryWrapper.select("date_calculated", type);                // 查询需要的字段
        List<StatisticsDaily> statisticsDailieList = baseMapper.selectList(statisticsDailyQueryWrapper);

        HashMap<String, Object> map = new HashMap<>();      // 存放map的List集合
        ArrayList<String> dateList = new ArrayList<>();     // 日期的list集合
        ArrayList<Integer> dataList = new ArrayList<>();    // 数据的List集合

        for (StatisticsDaily statisticsDaily : statisticsDailieList) {
            String dateCalculated = statisticsDaily.getDateCalculated();
            dateList.add(dateCalculated);
            // 判断查询的类型来进行封装需要的数据
            switch (type) {
                case "register_num":
                    dataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(statisticsDaily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(statisticsDaily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        // 将List数据存放到map集合中
        map.put("dataList", dataList);
        map.put("dateList", dateList);

        return map;
    }
}
