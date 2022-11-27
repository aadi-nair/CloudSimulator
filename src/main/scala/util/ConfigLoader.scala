package util
import com.typesafe.config.ConfigFactory
import util.*


object ConfigLoader:
  def loadHostConfig(configName: String, simName: String, hostIndex: Int): HostConfig =
    val conf = ConfigFactory.load(configName)
    val hostConfigPath = simName+".host"+hostIndex+"."
    HostConfig(conf.getInt(hostConfigPath+"ram"),conf.getInt(hostConfigPath+"storage"),conf.getInt(hostConfigPath+"bw"),conf.getInt(hostConfigPath+"pes"),conf.getInt(hostConfigPath+"mips"))

  def loadVmConfig(configName: String, simName: String, vmIndex: Int): VmConfig =
    val conf = ConfigFactory.load(configName)
    val vmConfigPath = simName + ".vm" + vmIndex + "."
    VmConfig(conf.getInt(vmConfigPath+"ram"),conf.getInt(vmConfigPath+"storage"),conf.getInt(vmConfigPath+"bw"),conf.getInt(vmConfigPath+"pes"),conf.getInt(vmConfigPath+"mips"), conf.getString(vmConfigPath+"vmm"))

  def loadCloudletConfig(configName: String, simName: String, cloudletIndex: Int): CloudletConfig =
    val conf = ConfigFactory.load(configName)
    val cloudletConfigPath = simName + ".cloudlet" + cloudletIndex + "."
    CloudletConfig(conf.getInt(cloudletConfigPath+"length"), conf.getInt(cloudletConfigPath+"pes"), conf.getInt(cloudletConfigPath+"size"))

  def loadDcConfig(configName: String, simName: String, dcIndex: Int): DcConfig =
    val conf = ConfigFactory.load(configName)
    val cloudletConfigPath = simName + ".datacenter" + dcIndex + "."
    DcConfig(conf.getInt(cloudletConfigPath+"numberOfHosts"), conf.getInt(cloudletConfigPath+"numberOfVms"), conf.getInt(cloudletConfigPath+"numberOfCloudlets"), conf.getInt(cloudletConfigPath+"costPerSec"), conf.getInt(cloudletConfigPath+"costPerMem"), conf.getInt(cloudletConfigPath+"costPerStorage"), conf.getInt(cloudletConfigPath+"costPerBw"), conf.getString(cloudletConfigPath+"arch"),conf.getString(cloudletConfigPath+"os"), conf.getString(cloudletConfigPath+"vmm") )


  def printCostConfig(costConfig: CostConfig): String =
     s"Processing Cost: ${costConfig.processingCost}" + "\n" +
      s"Memory Cost: ${costConfig.memoryCost}"+ "\n" +
      s"Storage Cost: ${costConfig.storageCost}" +"\n" +
      s"Bandwidth Cost: ${costConfig.bandwidthCost}" + "\n" +
      s"Total Cost: ${costConfig.totalCost}"

