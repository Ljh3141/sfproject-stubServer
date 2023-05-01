package yonam2023.stubFactoryServer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import yonam2023.stubFactoryServer.data.Machine;
import yonam2023.stubFactoryServer.data.MachineState;
import yonam2023.stubFactoryServer.service.MachineService;
import yonam2023.stubFactoryServer.service.StubRunning;

@Controller
public class FactoryController {

    @Autowired
    MachineService ms;

    @Autowired
    StubRunning st;

    private Logger logger = LoggerFactory.getLogger(FactoryController.class);

    @GetMapping("/cntCheck")
    public @ResponseBody String connectCheck(){
        return "true";
    }

    @GetMapping("/turnOn")
    public @ResponseBody String turnOnFactory(){
        if(st.getFactoryState()&&st.getPause()){
            st.resumeFactory();
            return "true";
        }
        else if(st.getFactoryState()){
            return "factory is already activated";
        }
        st.start();
        return "true";
    }
    @GetMapping("/pause")
    public @ResponseBody String pauseFactory(){
        if(st.getPause()){
            return "factory is already paused";
        }
        st.pauseFactory();
        return "factory successfully paused";
    }

    @GetMapping("/chkOperation")
    public  @ResponseBody String checkOperationState(){
        if(st.getFactoryState()&&!st.getPause()){
            return "true";
        }
        return "false";
    }

    @GetMapping("/shutdown")
    public @ResponseBody String shutdownFactory(){
        if(!st.getFactoryState()){
            return "factory is not activated";
        }
        st.off();
        return "factory successfully shut down";
    }

    @GetMapping("/isMcExist/{McId}")
    public @ResponseBody String isMachineExist(@PathVariable("McId")int id){
        //check MachineData and MachineExist
        if(ms.findMachine(id)==null) return "false";
        return "true";
    }

    @GetMapping("/checkMcState/{McId}")
    public @ResponseBody String checkMachineState(@PathVariable("McId")int id){
        //check and return Machine state
        //if not exist, send something different
        Machine m = ms.findMachine(id);
        if(m==null){
            return "machine not found";
        }
        return String.valueOf(m.getState());
    }

    @GetMapping("/runMachine/{McId}")
    @ResponseBody
    public String runMachineGet(@PathVariable("McId")int id){
        MachineState mstate = ms.runMachine(id);
        if(mstate == MachineState.NOTFOUND) {
            return "machine not found : "+ id;
        }else if(mstate == MachineState.RUN){
            return "machine already running";
        }else {
            return "true";
        }
    }
    @GetMapping("/stopMachine/{McId}")
    @ResponseBody
    public String stopMachineGet(@PathVariable("McId")int id){
        MachineState mstate = ms.stopMachine(id);
        if(mstate == MachineState.NOTFOUND) {
            return "machine not found : "+ id;
        }else if(mstate == MachineState.STOP){
            return "machine already stop : "+id;
        }else {
            return "true";
        }
    }
    @GetMapping("/serverNotActive")
    @ResponseBody
    public String serverNotActiveGet(){
        return "Server Not Active";
    }
}
