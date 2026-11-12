// Prueba de desarrollo: requiere Playwright fuera del material de estudio.
// PLAYWRIGHT_MODULE=/ruta/a/playwright node herramientas/probar-panel.cjs
const {chromium}=require(process.env.PLAYWRIGHT_MODULE||'playwright');
const path=require('path');
const fs=require('fs');
const root=path.resolve(__dirname,'..');
const review=path.resolve(root,'../../.impeccable/review');
const entry='file://'+path.join(root,'panel/index.html');
const assert=(ok,message)=>{if(!ok)throw Error(message);};
(async()=>{
 const browser=await chromium.launch({executablePath:process.env.CHROMIUM||'/usr/bin/chromium',headless:true,args:['--no-sandbox']});
 const page=await browser.newPage({viewport:{width:1440,height:1000},reducedMotion:'reduce'});
 const errors=[];page.on('pageerror',e=>errors.push(e.message));
 await page.goto(entry);await page.evaluate(()=>document.fonts.ready);
 const ids=await page.evaluate(()=>PSP.lessons.map(l=>l.id));
 for(const width of [1440,390]){
  await page.setViewportSize({width,height:width===390?844:1000});
  const routes=['inicio','teoria','comandos','guia','fuentes',...ids.flatMap(id=>['taller/'+id,'taller/'+id+'/practicar']),...await page.evaluate(()=>PSP.theory.map(t=>'pregunta/'+t.id)),...[0,1,2,3,'r'].map(i=>'simulacro/'+i)];
  for(const route of routes){
   await page.goto(entry+'#'+route);await page.waitForFunction(r=>location.hash==='#'+r,route);
   assert(await page.locator('h1').count()===1,'Falta título: '+route);
   assert(!await page.evaluate(()=>document.documentElement.scrollWidth>innerWidth+1),'Overflow: '+width+' '+route);
   if(route.startsWith('taller/')&&!route.endsWith('practicar')){
    const count=await page.locator('.timeline button').count();
    for(let i=1;i<count;i++)await page.locator('[data-action=next]').click();
    assert((await page.locator('[data-console="0"]').textContent()).includes('PADRE:codigo=0'),'La salida no llega al padre: '+route);
    assert(await page.locator('[data-action=next]').isDisabled(),'No termina escena');
    await page.locator('[data-action=previous]').click();
   }
  }
 }
 await page.goto(entry+'#pregunta/hilo');await page.locator('#answer').fill('Respuesta de prueba: pila y registros propios.');
 await page.locator('#reveal-answer').click();assert(await page.locator('#model').isVisible(),'Modelo no abre');
 await page.locator('#key-0').check();await page.reload();assert((await page.locator('#answer').inputValue()).includes('Respuesta de prueba'),'No persiste');
 await page.goto(entry+'#taller/02-directorios/practicar');await page.locator('#mastered').check();assert(await page.locator('#progress-count').textContent()==='1 / 9','Progreso');
 await page.goto(entry+'#comandos');await page.locator('#command-search').fill('imposible xyz');assert(await page.locator('.empty-state').isVisible(),'Estado vacío');
 await page.locator('#command-search').fill('javac');assert(await page.locator('.command-reference').count()>0,'Búsqueda');
 // Escena con error: el fallo se propaga hasta el padre.
 await page.goto(entry+'#taller/04-copiar');await page.locator('[data-mode=fail]').click();await page.locator('.timeline button').last().click();
 assert((await page.locator('[data-console="0"]').textContent()).includes('PADRE:codigo=1'),'El caso con error no propaga el código');
 // Ejercicios extra y modo Linux.
 await page.goto(entry+'#taller/03-buscar/practicar');assert(await page.locator('.exercise').count()===3,'Faltan ejercicios');
 await page.locator('[data-extra="03-buscar/2"]').check();await page.reload();assert(await page.locator('[data-extra="03-buscar/2"]').isChecked(),'Ejercicio no persiste');
 await page.locator('[data-platform=linux]').click();assert((await page.locator('#contenido').textContent()).includes('comprobar.sh talleres 03-buscar'),'Modo Linux sin verificador');
 await page.locator('[data-platform=win]').click();assert((await page.locator('#contenido').textContent()).includes('comprobar.cmd talleres 03-buscar'),'Modo Windows sin verificador');
 // Examen: preparar → examen → corregir.
 await page.goto(entry+'#simulacro/1');await page.locator('#exam-start').click();assert(await page.locator('#clock-toggle').textContent()==='Pausar','Reloj no arranca');
 assert(!await page.locator('.sidebar').isVisible(),'Sin modo concentración');assert(await page.locator('text=Respuesta modelo').count()===0,'Modelo visible durante el examen');
 await page.locator('#exam-answer-0').fill('Un proceso es una instancia en ejecución.');await page.locator('#exam-submit').click();
 assert(await page.locator('#score').textContent()==='0,0','Nota inicial');await page.locator('[data-key="0/0"]').check();await page.locator('[data-crit="0"]').check();
 assert(await page.locator('#score').textContent()!=='0,0','La nota no cambia');await page.goto(entry+'#simulacro/1');assert(await page.locator('#score').count()===1,'La corrección no persiste');
 await page.locator('#exam-again').click();assert(await page.locator('.history tbody tr').count()===1,'Historial');
 await page.goto(entry+'#simulacro/r');assert(await page.locator('#exam-start').count()===1,'Aleatorio');
 await page.goto(entry+'#pregunta/estados');await page.locator('[data-event=admit]').click();await page.locator('[data-event=cpu]').click();await page.locator('[data-event=io]').click();await page.locator('[data-event=ready]').click();assert(await page.locator('.state-node.active').textContent()==='Listo','Transición inválida');
 // Comprobar enlaces relativos presentes en cada pantalla sin navegar fuera.
 const missing=[];
 for(const route of ['inicio','guia','fuentes',...ids.map(id=>'taller/'+id+'/practicar')]){
  await page.goto(entry+'#'+route);
  const links=await page.locator('a[href]').evaluateAll(as=>as.map(a=>a.getAttribute('href')).filter(h=>h&&!h.startsWith('#')&&!h.includes('://')));
  for(const link of links){const target=path.resolve(root,'panel',decodeURIComponent(link.split('#')[0]));if(!fs.existsSync(target))missing.push(target);}
 }
 assert(missing.length===0,'Enlaces inexistentes: '+missing.join(', '));assert(errors.length===0,'Errores JS: '+errors.join(', '));
 fs.mkdirSync(review,{recursive:true});
 for(const [name,width,route,steps] of [['desktop',1440,'inicio',6],['mobile',390,'taller/04-copiar',9],['taller',1440,'taller/05-mover',11],['practicar',1440,'taller/04-copiar/practicar'],['examen',1440,'simulacro/2'],['correccion',1440,'simulacro/1'],['teoria',1440,'pregunta/hilo']]){
  await page.setViewportSize({width,height:width===390?844:1000});await page.goto(entry+'#'+route);await page.evaluate(()=>document.fonts.ready);
  if(steps)await page.locator('.timeline button').nth(steps).click();
  if(name==='examen'){await page.locator('#exam-start').click();await page.locator('#exam-answer-0').fill('Texto de prueba');}await page.screenshot({path:path.join(review,name+'.png'),fullPage:true});
 }
 const totalRoutes=5+ids.length*2+await page.evaluate(()=>PSP.theory.length+PSP.exams.length+1);
 const report={date:new Date().toISOString(),routes:totalRoutes+' por viewport',viewports:[1440,390],errors,missing,checks:['9 escenas completas con salida en el padre','escenario con error','ejercicios extra persistentes','selector Windows/Linux','examen: inicio, concentración, entrega, nota e historial','persistencia de respuesta','modelo desplegable','progreso','búsqueda y estado vacío','reloj y cierre de intento','transición bloqueado a listo','enlaces locales existentes']};
 fs.writeFileSync(path.join(review,'panel-tests.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));await browser.close();
})().catch(e=>{console.error(e);process.exit(1);});
