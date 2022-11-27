# Homework 3 (Aditya Nair - anair38)

CloudSimulation is an analysis project built using CloudSim Plus Library and Scala 3.1.3

## Project Structure

- `src/main/resources/`: 
  - contains `topology.brite` that describes a ring topology
  - has `logback.xml` used to run `slf4j` logger
  - contains all `.conf` files used to feed parameters to our Simulations
- `src/main/scala`: 
  - `simulation/`: contains all simulation files. Each defining its own `CloudSim` environment
  - `util/`: contains scala code to parse config files into plain `scala` classes
  - `Simulation.scala`: the driver code

## Components
1. **VmAllocationSimulation**: Simulation that runs a Datacenter which accepts `VmAllocationPolicy` that is substituted and used.
   - `VmAllocationPolicyRoundRobin`:  Assigns Vm in RoundRobin Algorithm. As seen from the log below 8 VMs are allocated to 4 Hosts 
   ```
    DatacenterBrokerSimple2: Sending Cloudlet 0 to Vm 0 in Host 0/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 1 to Vm 1 in Host 1/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 2 to Vm 2 in Host 2/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 3 to Vm 3 in Host 3/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 4 to Vm 4 in Host 0/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 5 to Vm 5 in Host 1/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 6 to Vm 6 in Host 2/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 7 to Vm 7 in Host 3/DC 1.
    ```
   - `VmAllocationPolicyFirstFit`: Assigns Vm by FirstFit Algorithm. Unlike RoundRobin VMs are assigned to fill up Hosts first
   ```
    DatacenterBrokerSimple2: Sending Cloudlet 0 to Vm 0 in Host 0/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 1 to Vm 1 in Host 0/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 2 to Vm 2 in Host 1/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 3 to Vm 3 in Host 1/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 4 to Vm 4 in Host 2/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 5 to Vm 5 in Host 2/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 6 to Vm 6 in Host 3/DC 1.
    DatacenterBrokerSimple2: Sending Cloudlet 7 to Vm 7 in Host 3/DC 1.
    ```




2. `CloudletSchedulerSimulation`: Simulation that shows the difference in different CloudletSchedulers
   - `CloudletSchedulerSpaceShared`: 

   ![Screenshot 2022-11-27 at 8 20 18 AM](https://user-images.githubusercontent.com/115327255/204140187-52111ce8-775d-4987-9465-4e786f54ffd1.png)

    Since 1 PE is being utilized by 2 Cloudlets, Cloudlet 2 waits for the execution of Cloudlet 1.

3. `NetworkTopologySimulation`: Uses `topology.brite` to create a Ring Topology between three `Datacenters` and a `DatacenterBroker`.
    The three Datacenters are individual implementations of `SaaS`, `PaaS` and `IaaS` .
    - `SaaS` utilizes a completely predefined Datacenter, Host, Vm and Cloudlets. So `placeholder.conf` is used to create a pre-defined datacenter.
    - `PaaS` gives the user the ability to deploy their own Cloudlets, So `placeholder.conf` is used to define Datacenter, Host and VM . But the cloudlet is configures using params in `paas.conf`. 
      `paas.conf` is considered as the user input.
    - `IaaS` gives complete autonomy of the system to the user. Datacenter, Host, Vm and the Cloudlets can be defined by the user. Therefore `iaas.conf` is used as parameters.
       `iaas.conf` is considered to be under the user's control.
      ![Screenshot 2022-11-27 at 8 33 49 AM](https://user-images.githubusercontent.com/115327255/204140798-55bd7c4c-3e2a-4903-9010-c357d5e04ccb.png)

    
      

## Dependencies
* Scala 3.1.3
* SBT 1.7.2
* slf4j-api 1.7.5
* typesafe config 1.3.3
* cloudsim-plus 6.4.3


## How to run this project
1. Clone this repository
   ```console
   foo@bar:~$ git clone https://github.com/aadi-nair/CloudSimulator.git
   foo@bar:~$ cd CloudSimulation
   ```
2. Clean and compile the project:
   ```console
   foo@bar:~$ sbt clean compile
   ```   
3. Run the scala project:
      ```console
      foo@bar:~$ sbt run
      ```
    Parts of the driver code can be commented for better visibility of each results.
    Additionally CloudletScheduling and VmAllocationPolicy can also be substituted with other availaible options or custom implementations