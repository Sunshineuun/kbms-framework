package com.winning.kbms.core.controller;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping ("/convertPicture")
public class ConvertPictureController extends BaseController
{
	@RequestMapping(value="/svg",method=RequestMethod.POST)
    private void svgServer(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyyMMdd");
		String dateTime=format.format(date);
        String svgString = request.getParameter("svg");
        String type = request.getParameter("type");
        String logName = new String(request.getParameter("logName").getBytes("ISO-8859-1"), "UTF-8");
        response.setContentType(type);
        String filename = logName+dateTime+"."+type.substring(6);
        response.setHeader("Content-disposition","attachment;filename=" + filename);
       
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,new Float(.8));
        TranscoderInput input = new TranscoderInput(new StringReader(svgString));
        try {
            TranscoderOutput output = new TranscoderOutput(response.getOutputStream());
            t.transcode(input, output);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }catch (Exception e){
            response.getOutputStream().close();
            e.printStackTrace();
        }
    }
}
