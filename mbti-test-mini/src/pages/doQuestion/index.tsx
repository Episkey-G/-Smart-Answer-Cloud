import { View, Image } from '@tarojs/components';
import headbg from '../../assets/headbg.jpeg';
// eslint-disable-next-line import/first
import { AtButton } from 'taro-ui';
import './index.scss';
import GlobalFooter from "../../components/GlobalFooter";

export default () => {
  return (
    <View className='indexPage'>
      <View className='at-article__h1 title'>
        MBTI 性格测试
      </View>
      <View className='at-article__h2 subTitle'>
        只需2分钟，就能非常准确地描述出你是谁，以及你的性格特点
      </View>
      <AtButton type='primary' circle className='enterBtn'>开始测试</AtButton>
      <Image className='headerBg' src={headbg} />
      <GlobalFooter />
    </View>
  );
};
