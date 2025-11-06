import SwiftUI
import MMKV

@main
struct iOSApp: App {
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
    
    init() {
        // 初始化MMKV
        initializeMMKV()
        }
    private func initializeMMKV() {
        // 设置MMKV根目录
        let rootDir = MMKV.initialize(rootDir: nil)
        print("MMKV root directory: \(rootDir)")
        
        // 获取默认MMKV实例
        guard let mmkv = MMKV.default() else {
            print("Failed to initialize MMKV")
            return
        }
        
        mmkv.set("abcdef", forKey: "test")
        
        print("MMKV initialized successfully")
        
        // 可选：设置加密密钥（用于敏感数据）
        // mmkv.setEncryptKey("your_encryption_key".data(using: .utf8))
    }
}
