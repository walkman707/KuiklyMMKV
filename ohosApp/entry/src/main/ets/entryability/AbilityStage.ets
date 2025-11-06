import AbilityStage from '@ohos.app.ability.AbilityStage';
import { MMKV } from '@tencent/mmkv';
import Napi from 'libentry.so';
import hilog from '@ohos.hilog';

export default class AbilityState extends AbilityStage {
  onCreate(): void {
    super.onCreate()
    // Napi.initKuikly();

    //直接调用鸿蒙版 MMKV 初始化，和 kmp 共用同一个
    MMKV.initialize(this.context.getApplicationContext())

    MMKV.defaultMMKV().encodeString("test", "abcdef")
    hilog.info(0x0000, 'mmkv', MMKV.defaultMMKV().decodeString("test"))
  }
}