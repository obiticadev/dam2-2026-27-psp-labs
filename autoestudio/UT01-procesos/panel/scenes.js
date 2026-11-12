/* Escenas DOM originales. No ejecutan Java, CMD ni solicitudes de red. */
(() => {
  const paths = {
    play:'M8 5l11 7-11 7z', pause:'M8 5v14M16 5v14', next:'M6 5l9 7-9 7M18 5v14',
    previous:'M18 5l-9 7 9 7M6 5v14', arrow:'M5 12h14m-5-5 5 5-5 5',
    code:'m8 7-5 5 5 5m8-10 5 5-5 5m-3-14-2 18',
    terminal:'m5 6 6 6-6 6m8 0h6', check:'m5 12 4 4L19 6',
    book:'M3 4h7l2 2 2-2h7v15h-7l-2 2-2-2H3zM12 6v15',
    home:'m3 11 9-8 9 8M5 10v11h5v-7h4v7h5V10',
    search:'M20 20l-5-5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0',
    menu:'M4 6h16M4 12h16M4 18h16', close:'m6 6 12 12M6 18 18 6',
    copy:'M8 8h12v12H8zM16 8V4H4v12h4',
    volume:'M4 9h4l5-5v16l-5-5H4zM17 8c3 2 3 6 0 8M19 4c6 4 6 12 0 16',
    full:'M4 9V4h5m6 0h5v5M4 15v5h5m6 0h5v-5',
    clock:'M12 6v6l4 2M22 12a10 10 0 1 1-20 0 10 10 0 0 1 20 0',
    download:'M12 3v12m-5-5 5 5 5-5M4 16v5h16v-5',
    folder:'M3 6V4h7l2 3h9v13H3z', file:'M6 3h8l4 4v14H6zM14 3v4h4',
    cpu:'M6 6h12v12H6zM9 9h6v6H9zM9 2v4m6-4v4M9 18v4m6-4v4M2 9h4m-4 6h4m12-6h4m-4 6h4',
    flag:'M5 21V4h12l-2 4 2 4H5', shuffle:'M3 7h4l10 10h4m0 0-3-3m3 3-3 3M3 17h4l3-3m4-4 3-3h4m0 0-3-3m3 3-3 3'
  };
  window.icon = (name) => `<svg viewBox="0 0 24 24" aria-hidden="true"><path d="${paths[name] || paths.code}"/></svg>`;
  const escapeHTML = window.escapeHTML = value => String(value).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));

  // Resaltado mínimo: comentario, cadena, palabra clave, tipo, número, método.
  const JAVA = [/(\/\/.*$)|("(?:\\.|[^"\\])*")|\b(public|private|static|final|void|new|while|if|else|return|throw|throws|try|catch|class|import|package|null|true|false|for|int|boolean|var)\b|\b([A-Z]\w*)\b|\b(\d+)\b|\b([a-z]\w*)(?=\s*\()/gm, ['com','str','kw','type','num','fn']];
  const SHELL = [/(^\s*(?:rem\b|#|::).*$)|("[^"\n]*"|'[^'\n]*')|(^\s*[\w.\\/-]+)|((?<=\s)(?:\/[A-Za-z?-]+|--?[\w-]+))|(%\w+%|\$\w+|\*\.\w+)|((?<=\s)(?:>>?|<|&&|\|\|?)(?=\s|"|$))/gm, ['com','str','kw','flag','var','op']];
  window.highlight = (src, lang = 'java') => {
    const [re, names] = lang === 'java' ? JAVA : SHELL;
    let out = '', last = 0;
    for (const m of String(src).matchAll(re)) {
      if (!m[0]) continue;
      const g = m.slice(1).findIndex(x => x !== undefined);
      out += escapeHTML(src.slice(last, m.index)) + `<span class="tk-${names[g]}">${escapeHTML(m[0])}</span>`;
      last = m.index + m[0].length;
    }
    return out + escapeHTML(src.slice(last));
  };

  const padreCode = (n, arg) => ['String cp = System.getProperty("java.class.path");','ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp,',`        "estudio.t${String(n).padStart(2,'0')}.Hijo", ${arg});`,'pb.redirectErrorStream(true);','Process hijo = pb.start();','BufferedReader br = new BufferedReader(','        new InputStreamReader(hijo.getInputStream()));','String linea;','while ((linea = br.readLine()) != null) {','    System.out.println(linea);','}','int codigo = hijo.waitFor();','System.out.println("PADRE:codigo=" + codigo);','System.exit(codigo);'];
  const STATE = {n:'Sin crear', c:'Preparado', r:'Ejecutando', l:'Leyendo', w:'Esperando', d:'Terminado · 0', x:'Terminado · error'};

  // Convierte el guion (etapas) en pasos con estados, salidas y disco calculados.
  function buildSteps(S, failMode) {
    const F = failMode ? S.fail : null, arg = F?.arg || S.arg, steps = [];
    const s = ['r','n','n']; let fs = (S.fs || []).filter(x => !F?.del?.includes(x)), px = S.stages[0].px, stage = 0;
    const push = (o, change) => {
      Object.entries(o.set || {}).forEach(([i, v]) => s[i] = v);
      const before = fs; if (change) fs = fs.filter(x => !(change.del || []).includes(x)).concat(change.add || []);
      steps.push({...o, st:s.join(''), px, stage, fs, fsAdd:change?.add || [], fsDel:(change?.del || []).filter(x => before.includes(x))});
    };
    const lines = (k, out, cls) => out.filter(x => !F?.del?.includes(x)).map(x => [k, x, cls]);
    push({t:'El padre prepara al hijo', d:`new ProcessBuilder solo guarda el comando: java, -cp, la clase Hijo y ${S.argName}. Todavía no existe ningún proceso nuevo.`, at:0, ln:'p1', set:{1:'c'}});
    push({t:'Fusiona errores antes de arrancar', d:'redirectErrorStream(true) junta stderr con stdout para leer un único flujo. Tiene que ir antes de start().', at:0, ln:'p3'});
    push({t:'start() crea la JVM hija', d:`El sistema operativo crea otro proceso Java y le entrega "${arg}" como un único argumento.`, at:1, ln:'p4', fl:'01', pkt:arg, set:{1:'r'}});
    push({t:'El padre se queda leyendo', d:'readLine() bloquea al padre hasta que el hijo escriba una línea o termine. No hace falta ningún hilo.', at:0, ln:'p8', set:{0:'l'}});
    const last = F ? F.stage : S.stages.length - 1; let code = 0;
    for (let i = 0; i <= last; i++) {
      const g = S.stages[i], failing = !!F && i === F.stage, tok = (failing && F.tok) || g.tok;
      px = g.px; stage = i;
      (g.pre || []).forEach(p => push({...p, at:1, ln:'h' + p.ln}));
      if (failing && F.start === false) {
        push({t:'start() lanza IOException', d:'La carpeta no existe: el sistema no puede crear el proceso. El comando nunca llega a ejecutarse y el hijo informa por stderr, que el padre lee fusionado.', at:1, ln:'h' + S.refs.start, out:lines(1, F.out, 'err')});
        push({t:'El error llega a tu terminal', d:'Un fallo de arranque no es un código de salida de CMD: es una excepción en Java. Aun así, el padre debe recibirlo.', at:0, ln:'p9', fl:'10', pkt:'IOException', out:lines(0, F.out, 'err'), set:{0:'r'}});
        code = F.code; break;
      }
      push({t:`El hijo lanza ${g.px}`, d:g.d, at:2, ln:'h' + (g.ln ?? S.refs.start), fl:'12', pkt:tok, out:[[2, '> ' + tok, 'cmd']], set:{2:'r'}});
      const out = failing ? F.out : (g.out || []);
      if (out.length) {
        push({t:`${g.px} escribe su salida`, d:failing ? 'El comando informa del error. Como stderr está fusionado, el hijo lo lee igual que una línea normal.' : (g.o || 'La salida queda en la tubería hacia el hijo hasta que alguien la lee.'), at:2, ln:'h' + S.refs.relay, out:lines(2, out, failing && 'err'), set:{1:'l'}});
        if (S.relay === 'full') {
          push({t:'El hijo reenvía cada línea', d:'Lee de getInputStream() y la reimprime con println: ahora la línea está en la salida del hijo.', at:1, ln:'h' + S.refs.relay, fl:'21', pkt:out.find(Boolean), out:lines(1, out, failing && 'err'), set:{1:'r'}});
          push({t:'Y llega a tu terminal', d:'El padre lee la salida de su hijo y la imprime. Es lo único que ves en CMD.', at:0, ln:'p9', fl:'10', pkt:out.find(Boolean), out:lines(0, out, failing && 'err'), set:{0:'r'}});
        } else {
          push({t:'La salida sube hasta tu terminal', d:'El hijo reimprime cada línea y el padre la lee de él: atraviesa dos tuberías antes de llegar a CMD.', at:0, ln:'p9', fl:'20', pkt:out.find(Boolean), out:[...lines(1, out, failing && 'err'), ...lines(0, out, failing && 'err')], set:{0:'r', 1:'r'}});
        }
      }
      code = failing ? F.code : 0;
      push({t:code ? `${g.px} termina con código ${code}` : `${g.px} termina · código 0`,
        d:code ? 'waitFor() devuelve un valor distinto de cero: el hijo no debe seguir con la etapa siguiente.' : i < last ? 'waitFor() devuelve 0: solo ahora empieza la etapa siguiente.' : 'Su salida se ha cerrado: readLine() devuelve null y waitFor() devuelve 0.',
        at:1, ln:'h' + S.refs.wait, set:{0:'l', 1:'w', 2:code ? 'x' : 'd'}}, failing ? null : {add:g.add, del:g.del});
      if (code) break;
    }
    push({t:code ? `El hijo sale con código ${code}` : 'El hijo termina', d:code ? 'System.exit(codigo) propaga el fallo hacia arriba en lugar de fingir éxito.' : 'Se cierra su salida: el readLine() del padre devuelve null y sale del bucle.', at:1, ln:'h' + (code ? S.refs.fail : S.exit), set:{1:code ? 'x' : 'd'}});
    push({t:`waitFor() devuelve ${code}`, d:'El padre recoge el código del hijo. Terminar y acertar no son lo mismo: lo decide ese número.', at:0, ln:'p11', set:{0:'w'}});
    push({t:'El padre informa y termina', d:code ? `Tu terminal muestra PADRE:codigo=${code} y el padre también sale con ${code}. El fallo no se ha escondido.` : 'PADRE:codigo=0 es la línea que comprueba el verificador. Ya no queda ningún proceso vivo.', at:0, ln:'p12', out:[[0, 'PADRE:codigo=' + code, code ? 'err' : 'ok']], set:{0:code ? 'x' : 'd'}});
    return steps;
  }

  const reduced = () => matchMedia('(prefers-reduced-motion: reduce)').matches;

  class ProcessScene {
    constructor(container, lesson) {
      const S = window.PSP_SCENES[lesson.id];
      this.container = container; this.lesson = lesson; this.S = S; this.fail = false;
      this.step = 0; this.timer = null; this.voice = false; this.speed = 1;
      this.steps = buildSteps(S, false);
      this.files = {p:padreCode(lesson.number, S.argName), h:S.hijo};
      const names = [['Padre','JVM · tu terminal CMD'],['Hijo','JVM lanzada por Padre'],[S.stages[0].px, S.stages.length > 1 ? 'una etapa cada vez' : 'proceso del sistema']];
      const lane = (i) => `<div class="proc" data-proc="${i}"><div class="proc-head"><strong class="proc-name">${escapeHTML(names[i][0])}</strong><span class="proc-state"></span></div><small class="proc-desc">${escapeHTML(names[i][1])}</small><small class="proc-pid"></small></div>`;
      const term = (i) => `<div class="console" data-console="${i}"><div class="console-title">${['Tu terminal · lo que imprime Padre','Salida de Hijo · la lee Padre','Salida del comando · la lee Hijo'][i]}</div><pre class="console-body"></pre></div>`;
      const pipe = (i) => `<div class="pipe" data-pipe="${i}" aria-hidden="true"><span class="pipe-line"></span><span class="packet"></span></div>`;
      const codeView = (f) => `<ol class="code-view" data-view="${f}" ${f === 'h' ? 'hidden' : ''}>${this.files[f].map((x, i) => `<li class="code-line" data-ln="${f}${i}"><span class="ln">${i + 1}</span><code>${x ? window.highlight(x) : ' '}</code></li>`).join('')}</ol>`;
      container.innerHTML = `<section class="scene" tabindex="-1" aria-label="Escena: ${escapeHTML(lesson.title)}">
        <div class="scene-top"><div><h2>Ejecución paso a paso</h2><p>Simulación · salidas ilustrativas de CMD en Windows</p></div>
          ${S.fail ? `<div class="segmented" role="group" aria-label="Escenario"><button data-mode="ok" aria-pressed="true">Caso correcto</button><button data-mode="fail" aria-pressed="false">Con error: ${escapeHTML(S.fail.label)}</button></div>` : ''}</div>
        <div class="proc-lanes">${lane(0)}${pipe(0)}${lane(1)}${pipe(1)}${lane(2)}${term(0)}${term(1)}${term(2)}</div>
        <div class="scene-main"><div class="scene-narration"><h3 class="scene-event" aria-live="polite"></h3><p class="scene-description"></p>
          ${S.fs ? `<div class="fs"><p class="fs-title">${icon('folder')}zona de pruebas</p><ul class="fs-tree"></ul></div>` : ''}</div>
          <div class="scene-code"><div class="code-tabs" role="tablist" aria-label="Archivo de código"><button role="tab" data-file="p" aria-selected="true">Padre.java</button><button role="tab" data-file="h" aria-selected="false">Hijo.java</button><span class="code-note">fragmento del patrón</span></div>${codeView('p')}${codeView('h')}</div></div>
        <div class="scene-controls">
          <button class="icon-button" data-action="previous" aria-label="Paso anterior">${icon('previous')}</button>
          <button class="icon-button play-button" data-action="play" aria-label="Reproducir escena">${icon('play')}</button>
          <button class="icon-button" data-action="next" aria-label="Paso siguiente">${icon('next')}</button>
          <div class="timeline" role="group" aria-label="Pasos de la escena"></div>
          <span class="step-count"></span>
          <label class="sr-only" for="scene-speed">Velocidad de reproducción</label><select id="scene-speed"><option value="0.6">0,6×</option><option value="1" selected>1×</option><option value="1.6">1,6×</option></select>
          <button class="icon-button" data-action="voice" aria-pressed="false" aria-label="Activar lectura en voz alta">${icon('volume')}</button>
          <button class="icon-button" data-action="full" aria-label="Ver escena a pantalla completa">${icon('full')}</button>
        </div>
        <p class="scene-caption">Teclado: ← → para avanzar, espacio para reproducir. El navegador no ejecuta comandos: la ejecución real está en tu terminal.</p>
      </section>`;
      this.root = container.querySelector('.scene');
      this.root.addEventListener('click', e => {
        const b = e.target.closest('button'); if (!b) return;
        if (b.dataset.file) return this.showFile(b.dataset.file);
        if (b.dataset.mode) {
          this.pause(); this.fail = b.dataset.mode === 'fail'; this.steps = buildSteps(S, this.fail); this.step = 0;
          this.root.querySelectorAll('[data-mode]').forEach(x => x.setAttribute('aria-pressed', String(x === b)));
          this.buildTimeline(); return this.render();
        }
        if (b.dataset.goto) { this.pause(); this.step = Number(b.dataset.goto); return this.render(); }
        const action = b.dataset.action;
        if (action === 'play') this.timer ? this.pause() : this.play();
        if (action === 'previous' || action === 'next') { this.pause(); this.go(action === 'next' ? 1 : -1); }
        if (action === 'voice') {
          if (!('speechSynthesis' in window)) { window.notify?.('Este navegador no ofrece lectura en voz alta.'); return; }
          this.voice = !this.voice; b.setAttribute('aria-pressed', String(this.voice));
          b.setAttribute('aria-label', this.voice ? 'Desactivar lectura en voz alta' : 'Activar lectura en voz alta');
          if (this.voice) this.speak(); else speechSynthesis.cancel();
        }
        if (action === 'full') {
          if (document.fullscreenElement) document.exitFullscreen?.();
          else if (this.root.requestFullscreen) this.root.requestFullscreen().catch(() => window.notify?.('La pantalla completa no está disponible; la escena sigue funcionando aquí.'));
          else window.notify?.('La pantalla completa no está disponible en este navegador.');
        }
      });
      this.root.addEventListener('keydown', e => {
        if (e.target.matches('select,input,textarea')) return;
        if (e.key === 'ArrowRight' || e.key === 'ArrowLeft') { e.preventDefault(); this.pause(); this.go(e.key === 'ArrowRight' ? 1 : -1); }
        if (e.key === ' ' && !e.target.matches('button')) { e.preventDefault(); this.timer ? this.pause() : this.play(); }
      });
      this.root.querySelector('select').addEventListener('change', e => { this.speed = Number(e.target.value); });
      this.onVisibility = () => { if (document.hidden) this.pause(); };
      document.addEventListener('visibilitychange', this.onVisibility);
      this.buildTimeline(); this.render();
    }
    buildTimeline() {
      this.root.querySelector('.timeline').innerHTML = this.steps.map((x, i) => `<button data-goto="${i}" aria-label="Paso ${i + 1}: ${escapeHTML(x.t)}" title="${escapeHTML(x.t)}"></button>`).join('');
    }
    go(delta) { this.step = Math.max(0, Math.min(this.steps.length - 1, this.step + delta)); this.render(); }
    showFile(f) {
      this.root.querySelectorAll('[data-view]').forEach(v => v.hidden = v.dataset.view !== f);
      this.root.querySelectorAll('[data-file]').forEach(b => b.setAttribute('aria-selected', String(b.dataset.file === f)));
    }
    render() {
      const x = this.steps[this.step], prev = this.steps[this.step - 1], $ = s => this.root.querySelector(s);
      $('.scene-event').textContent = x.t; $('.scene-description').textContent = x.d;
      // Procesos: estado, PID ilustrativo y foco del paso.
      this.root.querySelectorAll('[data-proc]').forEach(el => {
        const i = Number(el.dataset.proc), st = x.st[i];
        el.dataset.state = st; el.classList.toggle('is-active', x.at === i);
        el.querySelector('.proc-state').textContent = STATE[st];
        el.querySelector('.proc-pid').textContent = st === 'n' || st === 'c' ? (st === 'c' ? 'ProcessBuilder configurado' : 'Aún no existe') : `PID ${[4120, 4188, 4236 + x.stage * 28][i]}`;
        if (i === 2) el.querySelector('.proc-name').textContent = x.px;
      });
      // Consolas: se reconstruyen hasta el paso actual; lo nuevo se marca.
      const cons = [[], [], []];
      this.steps.slice(0, this.step + 1).forEach((s, k) => (s.out || []).forEach(([c, text, cls]) => cons[c].push(`<span class="${cls || ''}${k === this.step ? ' new' : ''}">${escapeHTML(text) || ' '}</span>`)));
      this.root.querySelectorAll('[data-console]').forEach(el => {
        const i = Number(el.dataset.console), body = el.querySelector('.console-body');
        body.innerHTML = cons[i].join('\n') || '<span class="empty">sin salida todavía</span>';
        el.classList.toggle('is-active', (x.out || []).some(o => o[0] === i)); body.scrollTop = body.scrollHeight;
      });
      // Tuberías: el paquete recorre la dirección real del dato.
      this.root.querySelectorAll('.packet').forEach(p => { p.getAnimations().forEach(a => a.cancel()); p.classList.remove('moving'); });
      if (x.fl && x !== this.lastFlow) this.flow(x);
      this.lastFlow = x;
      if (this.root.querySelector('.fs')) {
        const del = new Set(x.fsDel), add = new Set(x.fsAdd), list = [...new Set([...x.fs, ...x.fsDel])].sort((a, b) => a.replace(/\/$/, '').localeCompare(b.replace(/\/$/, '')));
        this.root.querySelector('.fs-tree').innerHTML = list.map(p => { const dir = p.endsWith('/'), parts = p.replace(/\/$/, '').split('/');
          return `<li class="${add.has(p) ? 'added' : ''}${del.has(p) ? 'removed' : ''}" style="--depth:${parts.length - 1}">${icon(dir ? 'folder' : 'file')}<span>${escapeHTML(parts.at(-1))}</span>${add.has(p) ? '<em>nuevo</em>' : del.has(p) ? '<em>eliminado</em>' : ''}</li>`; }).join('');
      }
      // Código: cambia al archivo que se ejecuta y centra la línea.
      const f = x.ln[0]; this.showFile(f);
      this.root.querySelectorAll('.code-line').forEach(el => el.classList.toggle('current', el.dataset.ln === x.ln));
      const view = this.root.querySelector(`[data-view="${f}"]`), line = view.querySelector(`[data-ln="${x.ln}"]`);
      if (line) view.scrollTo({top:Math.max(0, line.offsetTop - view.clientHeight / 3), behavior:reduced() || !prev ? 'auto' : 'smooth'});
      this.root.querySelectorAll('[data-goto]').forEach((b, i) => { b.classList.toggle('done', i < this.step); b.classList.toggle('current', i === this.step); b.toggleAttribute('aria-current', i === this.step); });
      $('.step-count').textContent = `Paso ${this.step + 1} / ${this.steps.length}`;
      $('[data-action=previous]').disabled = this.step === 0;
      $('[data-action=next]').disabled = this.step === this.steps.length - 1;
      if (this.voice) this.speak();
    }
    flow(x) {
      const legs = {'01':[[0, 1]], '12':[[1, 1]], '21':[[1, -1]], '10':[[0, -1]], '20':[[1, -1], [0, -1]]}[x.fl] || [];
      if (reduced()) { legs.forEach(([p]) => this.root.querySelector(`[data-pipe="${p}"]`).classList.add('pulse')); setTimeout(() => this.root.querySelectorAll('.pipe').forEach(p => p.classList.remove('pulse')), 600); return; }
      const label = String(x.pkt || '').length > 18 ? String(x.pkt).slice(0, 17) + '…' : String(x.pkt || '');
      legs.forEach(([p, dir], k) => {
        const pipe = this.root.querySelector(`[data-pipe="${p}"]`), el = pipe.querySelector('.packet'), vertical = matchMedia('(max-width: 1000px)').matches; // mismo corte que el CSS apilado
        el.textContent = label; el.classList.add('moving');
        const span = (vertical ? pipe.offsetHeight : pipe.offsetWidth) / 2 + 14, axis = vertical ? 'Y' : 'X';
        const from = `translate${axis}(${-dir * span}px)`, to = `translate${axis}(${dir * span}px)`;
        el.animate([{transform:from, opacity:0}, {opacity:1, offset:.2}, {opacity:1, offset:.8}, {transform:to, opacity:0}], {duration:900 / this.speed, delay:k * 820 / this.speed, easing:'cubic-bezier(.45,0,.2,1)', fill:'both'});
      });
    }
    speak() {
      if (!('speechSynthesis' in window)) return;
      speechSynthesis.cancel();
      const x = this.steps[this.step], u = new SpeechSynthesisUtterance(`${x.t}. ${x.d}`);
      u.lang = 'es-ES'; u.rate = .95; speechSynthesis.speak(u);
    }
    play() {
      if (this.step === this.steps.length - 1) { this.step = 0; this.render(); }
      const b = this.root.querySelector('[data-action=play]'); b.innerHTML = icon('pause'); b.setAttribute('aria-label', 'Pausar escena');
      const tick = () => {
        if (this.voice && window.speechSynthesis?.speaking) { this.timer = setTimeout(tick, 300); return; }
        if (this.step >= this.steps.length - 1) return this.pause(false);
        this.step++; this.render();
        this.timer = setTimeout(tick, (2000 + this.steps[this.step].d.length * 28) / this.speed);
      };
      this.timer = setTimeout(tick, 1200 / this.speed);
    }
    pause(cancelVoice = true) {
      clearTimeout(this.timer); this.timer = null;
      const b = this.root.querySelector('[data-action=play]'); b.innerHTML = icon('play'); b.setAttribute('aria-label', 'Reproducir escena');
      if (cancelVoice && 'speechSynthesis' in window) speechSynthesis.cancel();
    }
    destroy() { this.pause(); document.removeEventListener('visibilitychange', this.onVisibility); }
  }
  window.ProcessScene = ProcessScene;
  window.buildSceneSteps = buildSteps; // usado por la prueba del panel

  window.mountConcept = (container,type) => {
    if(type==='memory') {
      container.innerHTML=`<h3>La memoria ayuda a recordar la diferencia</h3><div class="memory-map"><div class="memory-process"><h4>Proceso A · un entorno</h4><div class="memory-shared">Código + datos + recursos compartidos</div><div class="memory-thread" data-thread="a1">Hilo A1 · pila y registros propios</div><div class="memory-thread" data-thread="a2">Hilo A2 · pila y registros propios</div></div><div class="memory-process"><h4>Proceso B · otro entorno</h4><div class="memory-shared">Su propio espacio de memoria</div><div class="memory-thread" data-thread="b1">Hilo B1 · pila y registros propios</div></div></div><div class="visual-controls"><button class="btn small secondary" data-show="shared">Ver lo compartido</button><button class="btn small secondary" data-show="private">Ver lo propio</button></div><p class="visual-description" aria-live="polite">Selecciona una parte para comparar recursos. El dibujo no es una medida real de memoria.</p>`;
      container.addEventListener('click',e=>{
        const b=e.target.closest('[data-show]');if(!b)return;
        container.querySelectorAll('.memory-thread').forEach(el=>el.classList.toggle('is-highlighted',b.dataset.show==='private'));
        container.querySelectorAll('.memory-shared').forEach(el=>el.style.background=b.dataset.show==='shared'?'var(--accent)':'');
        container.querySelector('.visual-description').textContent=b.dataset.show==='shared'?'A1 y A2 comparten el código, datos y recursos de A. B no los comparte automáticamente: necesitaría mecanismos explícitos.':'Cada hilo tiene sus registros y pila. Dos hilos no comparten una misma pila de ejecución.';
      });
    } else if(type==='cpu') {
      container.innerHTML=`<h3>Alternar no es ejecutar simultáneamente</h3><div class="cpu-demo"></div><div class="visual-controls"><button class="btn small secondary" data-cpu="1">Un núcleo</button><button class="btn small secondary" data-cpu="2">Dos núcleos</button><button class="btn small secondary" data-tick>Avanzar un intervalo</button></div><p class="visual-description" aria-live="polite"></p>`;
      let cores=1,tick=0;
      const render=()=>{
        const tracks=cores===1?[['A','B','A','B','A','B','A','B']]:[Array(8).fill('A'),Array(8).fill('B')];
        container.querySelector('.cpu-demo').innerHTML=tracks.map((row,i)=>`<small>Núcleo ${i+1} · intervalos ilustrativos</small><div class="cpu-track">${row.map((x,j)=>`<span class="${x==='B'?'task-b ':''}${j===tick?'active-slice':''}">${x}</span>`).join('')}</div>`).join('');
        container.querySelector('.visual-description').textContent=cores===1?'A y B avanzan alternándose. Son concurrentes; en un mismo instante solo una ocupa este núcleo.':'A y B pueden ejecutarse en el mismo intervalo sobre núcleos distintos. Este dibujo representa paralelismo, no una garantía de planificación real.';
      };
      container.addEventListener('click',e=>{const b=e.target.closest('button');if(!b)return;if(b.dataset.cpu){cores=Number(b.dataset.cpu);tick=0;}else if(b.hasAttribute('data-tick'))tick=(tick+1)%8;render();});render();
    } else if(type==='states') {
      const names=['Nuevo','Listo','Ejecución','Bloqueado','Terminado'];let current=0;
      container.innerHTML=`<h3>El estado responde a una causa</h3><div class="state-map">${names.map((n,i)=>`<span class="state-node" data-state="${i}">${n}</span>`).join('')}</div><div class="visual-controls"><button class="btn small secondary" data-event="admit">Admitir</button><button class="btn small secondary" data-event="cpu">Asignar CPU</button><button class="btn small secondary" data-event="io">Solicitar E/S</button><button class="btn small secondary" data-event="ready">Completar E/S</button><button class="btn small secondary" data-event="finish">Terminar</button><button class="text-button" data-event="reset">Reiniciar</button></div><p class="visual-description" aria-live="polite">Nuevo: aún no ha sido admitido.</p>`;
      const transitions={admit:[0,1,'El proceso ha sido admitido y espera CPU.'],cpu:[1,2,'El planificador le asigna CPU.'],io:[2,3,'Espera una operación de entrada/salida, no un turno de CPU.'],ready:[3,1,'La E/S ha terminado: vuelve a LISTO, no directamente a ejecución.'],finish:[2,4,'Ha terminado su ejecución.']};
      const paint=()=>container.querySelectorAll('[data-state]').forEach(el=>el.classList.toggle('active',Number(el.dataset.state)===current));
      container.addEventListener('click',e=>{const b=e.target.closest('[data-event]');if(!b)return;const name=b.dataset.event;let message;if(name==='reset'){current=0;message='Nuevo: aún no ha sido admitido.';}else{const t=transitions[name];if(current===t[0]){current=t[1];message=t[2];}else message=`No corresponde desde ${names[current]}. ${name==='cpu'&&current===3?'Completa primero la E/S: bloqueado no puede ocupar directamente la CPU.':'Sigue una transición válida del modelo de cinco estados.'}`;}container.querySelector('.visual-description').textContent=message;paint();});paint();
    } else {
      container.innerHTML=`<h3>Los flujos se nombran desde el padre</h3><div class="flow-map"><div class="flow-route"><span>Hijo escribe <strong>stdout</strong></span>${icon('arrow')}<span>Padre lee <code>getInputStream()</code></span></div><div class="flow-route"><span>Hijo escribe <strong>stderr</strong></span>${icon('arrow')}<span>Padre lee <code>getErrorStream()</code></span></div><div class="flow-route"><span>Padre escribe <code>getOutputStream()</code></span>${icon('arrow')}<span>Hijo recibe <strong>stdin</strong></span></div></div><p class="visual-description">Con redirectErrorStream(true), stdout y stderr del hijo se leen juntos mediante getInputStream(). Configúralo antes de start().</p>`;
    }
  };
})();
