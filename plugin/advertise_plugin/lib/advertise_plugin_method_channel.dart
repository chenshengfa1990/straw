import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'advertise_plugin_platform_interface.dart';

/// An implementation of [AdvertisePluginPlatform] that uses method channels.
class MethodChannelAdvertisePlugin extends AdvertisePluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('advertise_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> startPlayAd() async {
    return await methodChannel.invokeMethod('startPlayAd');
  }
}
