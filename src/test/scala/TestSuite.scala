import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.Datacenter
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import org.cloudbus.cloudsim.schedulers.vm.{VmScheduler, VmSchedulerSpaceShared}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull
import org.cloudbus.cloudsim.vms.Vm
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import util.{CloudletConfig, ConfigLoader, DatacenterLoader, DcConfig, HostConfig, VmConfig}

class TestSuite extends AnyFlatSpec with Matchers {

  val simulation = new CloudSim()
  val broker = new DatacenterBrokerSimple(simulation)
  val vmConfig: VmConfig = ConfigLoader.loadVmConfig("test.conf", "simulation1", 1)
  val hostConfig: HostConfig = ConfigLoader.loadHostConfig("test.conf", "simulation1", 1)
  val cloudletConfig: CloudletConfig = ConfigLoader.loadCloudletConfig("test.conf", "simulation1", 1)
  val dcConfig: DcConfig = ConfigLoader.loadDcConfig("test.conf", "simulation1", 1)
  val dcLoader: DatacenterLoader = DatacenterLoader(hostConfig, dcConfig, vmConfig, cloudletConfig)


  it should("Accurately parse VM Configurations") in {

    assert(vmConfig.mips == 1000)
    assert(vmConfig.pes == 1)
    assert(vmConfig.ram == 1024)
    assert(vmConfig.bandwidth == 1000)
    assert(vmConfig.storage == 2000)
  }

  it should ("Accurately parse Host Configurations") in {

    assert(hostConfig.ram == 4096)
    assert(hostConfig.storage == 200000)
    assert(hostConfig.bandwidth == 1000000)
    assert(hostConfig.pes == 1)
    assert(hostConfig.mips == 1000)
  }

  it should ("Accurately parse Cloudlet Configurations") in {

    assert(cloudletConfig.length == 2000)
    assert(cloudletConfig.pes == 2)
    assert(cloudletConfig.size == 512)
  }

  it should ("Accurately parse Datacenter Configurations") in {
    assert(dcConfig.noOfHosts == 2)
    assert(dcConfig.noOfVms == 4)
    assert(dcConfig.noOfCloudlets == 6)
    assert(dcConfig.arch == "x86")
    assert(dcConfig.os == "Linux")
    assert(dcConfig.vmm == "Vmware")
    assert(dcConfig.costPerBw ==  0.0006)
    assert(dcConfig.costPerSec ==  1.5)
    assert(dcConfig.costPerMem ==  0.05)
    assert(dcConfig.costPerStorage ==  0.001)
    
  }

  it should("create a Datacenter given configuration files") in {
    assert(dcLoader.loadDatacenter(simulation, VmAllocationPolicySimple(), VmSchedulerSpaceShared(), true).isInstanceOf[Datacenter])
  }


  it should("create VmList given configuration files") in {
    assert(dcLoader.vmList(CloudletSchedulerSpaceShared()).isInstanceOf[List[Vm]])
  }

  it should ("create CloudletList given configuration files") in {
    assert(dcLoader.cloudletList(UtilizationModelFull()).isInstanceOf[List[Cloudlet]])
  }
}
