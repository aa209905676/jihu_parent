package com.atguigu.guli.service.statistics.controller.admin;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags="统计分析管理")
//@CrossOrigin
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {

    @Resource
    private DailyService dailyService;

    @ApiOperation("生成统计记录")
    @PostMapping("create/{day}")
    public R createStatisticsByDay(
            @ApiParam("统计日期")
            @PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok().message("数据统计生成成功");
    }

    @ApiOperation("显示统计数据")
    @GetMapping("show-chart/{begin}/{end}")
    public R showChart(
            @ApiParam("开始时间") @PathVariable String begin,
            @ApiParam("结束时间") @PathVariable String end){

        Map<String, Map<String, Object>> map = dailyService.getChartData(begin, end);
        return R.ok().data("chartData", map);
    }
}