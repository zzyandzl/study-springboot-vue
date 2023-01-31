<template>
  <div>
    <el-row style="margin-top: 25px; text-align: center">
      <el-col :span="5">
        <el-card style="color: #409EEF">
          <div><i class="el-icon-user-solid"></i>用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            1000
          </div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card style="color: #F56C6C; margin-left: 20px;">
          <div><i class="el-icon-money"></i>销售总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">10000</div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card style="color: #67C23A; margin-left: 20px;">
          <div><i class="el-icon-bank-card"></i>收益总额</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">￥1000000</div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card style="color: #E6A23C; margin-left: 20px;">
          <div><i class="el-icon-s-shop"></i>门店总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">100</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 25px;">
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 400px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px;"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

/*引入echarts*/
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
    return {

    }
  },
  mounted() {//页面元素渲染之后再触发
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option = {
      title: {
        text: '各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          //折线图
          // data: [150,230,224,218],
          data: [],
          type: 'line'
        },
        {
          //柱状图
          // data: [150,230,224,218],
          data: [],
          type: 'bar'
        }
      ]
    };

    // myChart.setOption(option);
    this.request.get("/echarts/members").then(res => {
      // 填空
      // option.xAxis.data = res.data().x
      option.series[0].data = res.data
      option.series[1].data = res.data
      myChart.setOption(option);
    })

    // 饼图
    var pieOption = {
      title: {
        text: '各季度会员数量统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{d}%'
            }
          },
          data: [],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);
    this.request.get("/echarts/members").then(res => {
      pieOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]},
      ]
      pieChart.setOption(pieOption);
    })
  },
}
</script>

<style scoped>

</style>