package simulation
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicyFirstFit, VmAllocationPolicySimple}
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
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology
import org.cloudbus.cloudsim.network.topologies.NetworkTopology

import scala.jdk.CollectionConverters.*
import java.util

class NetworkTopologySimulation:

  val logger: Logger = CreateLogger(classOf[NetworkTopologySimulation])
  val simulation: CloudSim = CloudSim()
  val placeholderConfig ="placeholder.conf"
  val paasCloudletConfig = "paas.conf"
  val iaasCloudletConfig = "iaas.conf"
  val NETWORK_TOPOLOGY = "topology.brite"


  // SAAS
  val saasHostConfiguration: HostConfig = ConfigLoader.loadHostConfig(placeholderConfig, "simulation1", 1)
  val saasCloudletConfiguration: CloudletConfig = ConfigLoader.loadCloudletConfig(placeholderConfig, "simulation1", 1)
  val saasVmConfiguration: VmConfig = ConfigLoader.loadVmConfig(placeholderConfig, "simulation1", 1)
  val saasDcConfiguration: DcConfig = ConfigLoader.loadDcConfig(placeholderConfig, "simulation1", 1)

  val saasDcLoader: DatacenterLoader = DatacenterLoader(saasHostConfiguration, saasDcConfiguration, saasVmConfiguration, saasCloudletConfiguration)
  val saas_datacenter: Datacenter = saasDcLoader.loadDatacenter(simulation, VmAllocationPolicyFirstFit(), VmSchedulerTimeShared(), true)


  // PAAS
  val paasHostConfiguration: HostConfig = ConfigLoader.loadHostConfig(placeholderConfig, "simulation1", 1)
  /*
  User Specified Cloudlet
  */
  val paasCloudletConfiguration: CloudletConfig = ConfigLoader.loadCloudletConfig(paasCloudletConfig, "simulation1", 1)
  val paasVmConfiguration: VmConfig = ConfigLoader.loadVmConfig(placeholderConfig, "simulation1", 1)
  val paasDcConfiguration: DcConfig = ConfigLoader.loadDcConfig(placeholderConfig, "simulation1", 1)

  val paasDcLoader: DatacenterLoader = DatacenterLoader(paasHostConfiguration, paasDcConfiguration, paasVmConfiguration, paasCloudletConfiguration)
  val paas_datacenter: Datacenter = paasDcLoader.loadDatacenter(simulation, VmAllocationPolicyFirstFit(), VmSchedulerTimeShared(), true)


  //IAAS
  val iaasHostConfiguration: HostConfig = ConfigLoader.loadHostConfig(iaasCloudletConfig, "simulation1", 1)
  val iaasCloudletConfiguration: CloudletConfig = ConfigLoader.loadCloudletConfig(iaasCloudletConfig, "simulation1", 1)
  val iaasVmConfiguration: VmConfig = ConfigLoader.loadVmConfig(iaasCloudletConfig, "simulation1", 1)
  val iaasDcConfiguration: DcConfig = ConfigLoader.loadDcConfig(iaasCloudletConfig, "simulation1", 1)

  val iaasDcLoader: DatacenterLoader = DatacenterLoader(iaasHostConfiguration, iaasDcConfiguration, iaasVmConfiguration, iaasCloudletConfiguration)
  val iaas_datacenter: Datacenter = iaasDcLoader.loadDatacenter(simulation, VmAllocationPolicyFirstFit(), VmSchedulerTimeShared(), true)

  val broker: DatacenterBroker = DatacenterBrokerSimple(simulation)

  arrangeDatacenters()

  broker.submitVmList((saasDcLoader.vmList(CloudletSchedulerTimeShared()) ++
    paasDcLoader.vmList(CloudletSchedulerTimeShared()) ++
    iaasDcLoader.vmList(CloudletSchedulerTimeShared())).asJava )

  broker.submitCloudletList((saasDcLoader.cloudletList(UtilizationModelFull()) ++
    paasDcLoader.cloudletList(UtilizationModelFull()) ++
    iaasDcLoader.cloudletList(UtilizationModelFull())).asJava)


  simulation.start()
  val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList()
  new CloudletsTableBuilder(finishedCloudlets).build()
  logger.info("--------Cost -------")
  val totalCost = finishedCloudlets.asScala.foldLeft(0.0)((acc, cloudLet) => acc + cloudLet.getTotalCost)
  logger.info(s"Total Cost: $totalCost")





  def arrangeDatacenters(): Unit ={

    val networkTopology = BriteNetworkTopology.getInstance(NETWORK_TOPOLOGY)
    simulation.setNetworkTopology(networkTopology)

    //Datacenter1 mapped to BRITE node 0
    networkTopology.mapNode(iaas_datacenter, 0)

    //Datacenter2 mapped to BRITE node 2
    networkTopology.mapNode(paas_datacenter, 2)

    //Datacenter3 mapped to BRITE node 3
    networkTopology.mapNode(saas_datacenter, 3)

    //Broker mapped to BRITE node 4
    networkTopology.mapNode(broker, 4)

  }

