import { View, Image } from '@tarojs/components';
import headbg from '../../assets/headbg.jpeg';
// eslint-disable-next-line import/first
import { AtButton } from 'taro-ui';
import './index.scss';

export default () => {
  return (
    <View className='index'>
      <View className='at-article__h1'>
        这是一级标题这是一级标题
      </View>
      <View className='at-article__h2'>
        这是一级标题这是一级标题
      </View>
      <AtButton type='primary' circle>按钮文案</AtButton>
      <Image src={headbg} />
    </View>
  );
};
