/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { InteractionRecordUpdateComponent } from 'app/entities/interaction-record/interaction-record-update.component';
import { InteractionRecordService } from 'app/entities/interaction-record/interaction-record.service';
import { InteractionRecord } from 'app/shared/model/interaction-record.model';

describe('Component Tests', () => {
    describe('InteractionRecord Management Update Component', () => {
        let comp: InteractionRecordUpdateComponent;
        let fixture: ComponentFixture<InteractionRecordUpdateComponent>;
        let service: InteractionRecordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [InteractionRecordUpdateComponent]
            })
                .overrideTemplate(InteractionRecordUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InteractionRecordUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InteractionRecordService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InteractionRecord(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.interactionRecord = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InteractionRecord();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.interactionRecord = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
