import { View, Image } from "@tarojs/components";
import headbg from "../../assets/headbg.jpeg";
// eslint-disable-next-line import/first
import { AtButton } from "taro-ui";
import "./index.scss";
import questionResults from "../../data/question_results.json";
import questions from "../../data/questions.json";
import GlobalFooter from "../../components/GlobalFooter";
// eslint-disable-next-line import/first
import Taro from "@tarojs/taro";
import {getBestQuestionResult} from "../../utils/bizUtils";

/**
 * 测试结果页面
 */
export default () => {
  const answerList = Taro.getStorageSync('answerList');

  //获取答案
  if(!answerList || answerList.length < 1){
    Taro.showToast({
      title: '答案为空',
      icon: 'error',
      duration: 3000
    })
  }
  //计算结果
  const result = getBestQuestionResult(answerList, questions, questionResults);
  return (
    <View className='resultPage'>
      <View className='at-article__h1 title'>{result.resultName}</View>
      <View className='at-article__h2 subTitle'>{result.resultDesc}</View>
      <AtButton
        type='primary'
        circle
        className='enterBtn'
        onClick={() => {
          Taro.reLaunch({
            url: "/pages/index/index",
          });
        }}
      >
        返回主页
      </AtButton>
      <Image className='headerBg' src={headbg} />
      <GlobalFooter />
    </View>
  );
};
