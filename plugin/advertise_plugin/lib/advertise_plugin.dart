
import 'advertise_plugin_platform_interface.dart';

class AdvertisePlugin {
  Future<String?> getPlatformVersion() {
    return AdvertisePluginPlatform.instance.getPlatformVersion();
  }

  Future<void> startPlayAd() {
    return AdvertisePluginPlatform.instance.startPlayAd();
  }
}
