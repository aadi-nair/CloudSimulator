import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyFirstFit, VmAllocationPolicyRandom, VmAllocationPolicyRoundRobin}
import org.cloudbus.cloudsim.distributions.UniformDistr
import util.*
import simulation.{CloudletSchedulerSimulation, NetworkTopologySimulation, VmAllocationSimulation}
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.slf4j.Logger



object Simulation:
  def main(args: Array[String]): Unit =
    val logger: Logger = CreateLogger(this.getClass)
    logger.info("--------------CloudletSchedulerSpaceShared---------------")
    CloudletSchedulerSimulation("space_shared_simulation.conf", "simulation1", CloudletSchedulerSpaceShared())
    logger.info("--------------VmAllocationPolicyRoundRobin---------------")
    VmAllocationSimulation("allocation_simulation.conf", "simulation1", VmAllocationPolicyRoundRobin())
    logger.info("--------------VmAllocationPolicyFirstFit---------------")
    VmAllocationSimulation("allocation_simulation.conf", "simulation1", VmAllocationPolicyFirstFit())
    logger.info("--------------SaaS PaaS and Iaas in Ring Topology ---------------")
    NetworkTopologySimulation()




