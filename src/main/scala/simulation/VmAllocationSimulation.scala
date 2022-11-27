package simulation

import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.{DatacenterBroker, DatacenterBrokerSimple}
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.Datacenter
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModel, UtilizationModelFull}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.slf4j.Logger
import util.{CloudletConfig, ConfigLoader, CreateLogger, DatacenterLoader, DcConfig, HostConfig, VmConfig}
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared


import java.util
import scala.jdk.CollectionConverters.*

class VmAllocationSimulation(val configName: String, val simulationName: String, val allocationPolicy: VmAllocationPolicy) :
  val logger: Logger = CreateLogger(classOf[VmAllocationSimulation])
  val simulation: CloudSim = CloudSim()

  val hostConfiguration: HostConfig = ConfigLoader.loadHostConfig(configName, simulationName, 1)
  val cloudletConfiguration: CloudletConfig = ConfigLoader.loadCloudletConfig(configName, simulationName, 1)
  val vmConfiguration: VmConfig = ConfigLoader.loadVmConfig(configName, simulationName, 1)
  val dcConfiguration: DcConfig = ConfigLoader.loadDcConfig(configName, simulationName, 1)

  val dcLoader: DatacenterLoader = DatacenterLoader(hostConfiguration, dcConfiguration, vmConfiguration, cloudletConfiguration)
  val datacenter: Datacenter = dcLoader.loadDatacenter(simulation, allocationPolicy, VmSchedulerTimeShared(), false)

  val broker = DatacenterBrokerSimple(simulation)

  broker.submitVmList(dcLoader.vmList(CloudletSchedulerTimeShared()).asJava)
  broker.submitCloudletList(dcLoader.cloudletList(UtilizationModelFull()).asJava)

  simulation.start()
  val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList()
  new CloudletsTableBuilder(finishedCloudlets).build()
  logger.info(ConfigLoader.printCostConfig(dcLoader.calculateCost(broker)))




