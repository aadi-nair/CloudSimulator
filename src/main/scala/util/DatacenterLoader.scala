package util
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy
import org.cloudbus.cloudsim.brokers.DatacenterBroker
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.{Datacenter, DatacenterSimple}
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel
import org.cloudbus.cloudsim.vms.{Vm, VmSimple, VmCost}
import org.slf4j.Logger
import util.CreateLogger

import scala.jdk.CollectionConverters.*


class DatacenterLoader(val hostConfig: HostConfig, val dcConfig: DcConfig, val vmConfig: VmConfig, val cloudletConfig: CloudletConfig):
  val logger: Logger = CreateLogger(classOf[DatacenterLoader])

  def peList(): List[Pe] =
    logger.info(s"creating ${hostConfig.pes} PE with ${hostConfig.mips} MIPS for Host" )
    List.range(0, hostConfig.pes).map((_)=> PeSimple(hostConfig.mips))


  def hostList(activeHosts: Boolean, vmScheduler: VmScheduler): List[Host] =
    logger.info(s"creating ${dcConfig.noOfHosts} Hosts with ram : ${hostConfig.ram} bw: ${hostConfig.bandwidth}, storage: ${hostConfig.storage} with ${vmScheduler.getClass.getSimpleName}" )
    List.range(0, dcConfig.noOfHosts)
      .map((_)=> HostSimple(hostConfig.ram, hostConfig.bandwidth, hostConfig.storage, peList().asJava, activeHosts)
        .setVmScheduler(vmScheduler.getClass.getDeclaredConstructor().newInstance()))


  def loadDatacenter(sim: CloudSim, vmAllocationPolicy: VmAllocationPolicy, vmScheduler: VmScheduler,  activeHosts: Boolean ): Datacenter =
    logger.info(s"creating datacenter with ${vmAllocationPolicy.getClass.getSimpleName} ")

    val dc = DatacenterSimple(sim, hostList(activeHosts, vmScheduler).asJava, vmAllocationPolicy.getClass.getDeclaredConstructor().newInstance())
    dc.getCharacteristics()
      .setArchitecture(dcConfig.arch)
      .setOs(dcConfig.os)
      .setVmm(dcConfig.vmm)
      .setCostPerSecond(dcConfig.costPerSec)
      .setCostPerMem(dcConfig.costPerMem)
      .setCostPerStorage(dcConfig.costPerStorage)
      .setCostPerBw(dcConfig.costPerBw)
    dc


  def vmList(cloudletScheduler: CloudletScheduler): List[Vm] =
    logger.info(s"creating ${dcConfig.noOfVms} VMs with mips: ${vmConfig.mips} pes: ${vmConfig.pes} ram: ${vmConfig.ram} bw: ${vmConfig.bandwidth}, storage: ${vmConfig.storage} ${cloudletScheduler.getClass.getSimpleName}")
    List.fill(dcConfig.noOfVms)(VmSimple(vmConfig.mips, vmConfig.pes, cloudletScheduler.getClass.getDeclaredConstructor().newInstance())
      .setRam(vmConfig.ram)
      .setBw(vmConfig.bandwidth)
      .setSize(vmConfig.storage))


  def cloudletList(utilizationModel: UtilizationModel): List[Cloudlet] =
    cloudletList(utilizationModel,utilizationModel,utilizationModel)

  def cloudletList(cpuUtilizationModel: UtilizationModel, ramUtilizationModel:UtilizationModel, bwUtilizationModel:UtilizationModel ): List[Cloudlet] =
    logger.info(s"creating ${dcConfig.noOfCloudlets} cloudlets with len: ${cloudletConfig.length} pe: ${cloudletConfig.pes} and cpuUtilModel: ${cpuUtilizationModel.getClass.getSimpleName} ramUtilModel: ${ramUtilizationModel.getClass.getSimpleName} bwUtilModel: ${bwUtilizationModel.getClass.getSimpleName}")
    List.fill(dcConfig.noOfCloudlets)(CloudletSimple(cloudletConfig.length, cloudletConfig.pes)
      .setSizes(cloudletConfig.size)
      .setUtilizationModelCpu(cpuUtilizationModel)
      .setUtilizationModelBw(bwUtilizationModel)
      .setUtilizationModelRam(ramUtilizationModel)
    )


  def calculateCost(broker: DatacenterBroker): CostConfig =
    val vms = broker.getVmCreatedList().asScala
    val totalProcessingCost = vms.foldLeft(0.0)((acc, vm) => acc + VmCost(vm).getProcessingCost())
    val totalMemoryCost = vms.foldLeft(0.0)((acc, vm) => acc + VmCost(vm).getMemoryCost())
    val totalStorageCost = vms.foldLeft(0.0)((acc, vm) => acc + VmCost(vm).getStorageCost())
    val totalBwCost = vms.foldLeft(0.0)((acc, vm) => acc + VmCost(vm).getBwCost())
    val totalCost = vms.foldLeft(0.0)((acc, vm) => acc + VmCost(vm).getTotalCost())

    CostConfig(totalProcessingCost, totalMemoryCost, totalStorageCost, totalBwCost, totalCost)




