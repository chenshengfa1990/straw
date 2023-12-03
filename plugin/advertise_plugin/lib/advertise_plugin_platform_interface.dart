import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'advertise_plugin_method_channel.dart';

abstract class AdvertisePluginPlatform extends PlatformInterface {
  /// Constructs a AdvertisePluginPlatform.
  AdvertisePluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static AdvertisePluginPlatform _instance = MethodChannelAdvertisePlugin();

  /// The default instance of [AdvertisePluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelAdvertisePlugin].
  static AdvertisePluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AdvertisePluginPlatform] when
  /// they register themselves.
  static set instance(AdvertisePluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> startPlayAd();
}
